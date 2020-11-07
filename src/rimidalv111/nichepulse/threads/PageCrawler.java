package rimidalv111.nichepulse.threads;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rimidalv111.nichepulse.moz.MozLookup;
import rimidalv111.ypaa.main.VideoInfo;

public class PageCrawler extends Thread
{

	public ThreadsHandler instance;
	private VideoInfo keyword;

	public PageCrawler(ThreadsHandler i, VideoInfo k)
	{
		instance = i;
		keyword = k;
	}

	public void run()
	{
		String request = "https://www.google.com/search?q=" + keyword.getVideo_Keyword() + "&num=10";
		System.out.println("Page Crawler > Opening Connection > " + request);

		try
		{

			Document doc = Jsoup.connect(request).userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)").timeout(5000).get();


			Elements tube_results = doc.select("table.ts");

			int count = 0;

			for(Element tr : tube_results) //retrieve video image url, video url, and video title
			{
				count++;

				try
				{

					Element image = tr.select("img[src]").first();

					if(image != null)
					{
						String image_url = image.attr("src");

						if(image_url.startsWith("https://img.youtube.com"))
						{
							//VIDEO IMAGE URL
							if(count <= 1)
							{
								keyword.setVideo_img_url(image_url);
							}
							

							String video_id = image_url.substring(image_url.indexOf("vi/") + 3, image_url.indexOf("/default"));

							String video_url = "https://www.youtube.com/watch?v=" + video_id;

							if(count <= 1)
							{
								keyword.setVideo_url(video_url);
							} else
							{
								keyword.getExtra_urls().add(video_url);
							}


						}

						String video_title = tr.select("tr").first().text();

						video_title = video_title.replace(" - YouTube", "");

						if(count <= 1)
						{
							keyword.setVideo_title(video_title);
						}


					} else
					{
						System.out.println("Page Crawler Failed > Couldn't find the src node > No Video on Page");

						continue;
					}
				} catch(Exception io)
				{
					System.out.println("Page Crawler Failed > No Video on Page: continuing");

					continue;
				}

			}

			(new MozLookup(instance, keyword)).start();

			softKillTheThread();

		} catch(Exception e)
		{
			System.out.println("Page Crawler Failed > Bad IP");

			instance.getInstance().changeIP();

			e.printStackTrace();
			softKillTheThread();
		}
	}

	public void killTheThread()
	{
		instance.running_search = false;
		instance.runNextKeyword();
	}

	public void softKillTheThread()
	{
		instance.running_search = false;
		instance.getInstance().updateProgressBar();
		this.interrupt();
	}

}
