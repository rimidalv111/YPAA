package rimidalv111.nichepulse.moz;

import rimidalv111.nichepulse.threads.ThreadsHandler;
import rimidalv111.ypaa.main.VideoInfo;

import com.google.gson.Gson;
import com.seomoz.api.response.UrlResponse;

public class MozLookup extends Thread
{
	private ThreadsHandler threadsHandler;
	private VideoInfo url;

	public MozLookup(ThreadsHandler th, VideoInfo u)
	{
		threadsHandler = th;
		url = u;
	}

	public void run()
	{
		try
		{
			if(threadsHandler.mozAuth() != null)
			{
				System.out.println("Moz Lookup > Sending Request[s]");
				String ur = url.getVideo_url();

				String response = threadsHandler.getUrlMetricsService().getUrlMetrics(ur);
				Gson gson = new Gson();
				UrlResponse res = gson.fromJson(response, UrlResponse.class);

				//res.getUpa(); Page Authority
				//res.getPda(); Domain Authority

				int pa = (int) Math.round(Double.parseDouble(res.getUpa()));

				url.setPage_authority(pa);

				if(url.getExtra_urls().size() >= 1)
				{
					for(int i = 0; i < url.getExtra_urls().size(); i++)
					{
						Thread.sleep(6000);
						String response2 = threadsHandler.getUrlMetricsService().getUrlMetrics(url.getExtra_urls().get(i));
						Gson gson2 = new Gson();
						UrlResponse res2 = gson2.fromJson(response2, UrlResponse.class);

						int pa2 = (int) Math.round(Double.parseDouble(res2.getUpa()));
						url.getExtra_pa().add(pa2 + "");
					}
				}
				

			} else
			{
				url.setPage_authority(-1);
			}

			threadsHandler.handle_completed_Keyword(url);

			killTheThread();

		} catch(Exception io)
		{
			url.setPage_authority(-1);

			threadsHandler.handle_completed_Keyword(url);

			System.out.println("Moz Lookup Failed > No Moz Data For That Keyword");

			killTheThread();
		}
	}

	public void killTheThread()
	{
		threadsHandler.running_search = false;
		threadsHandler.runNextKeyword();
		this.interrupt();
	}
}
