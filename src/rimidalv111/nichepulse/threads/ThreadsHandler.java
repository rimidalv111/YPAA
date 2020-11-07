package rimidalv111.nichepulse.threads;

import java.util.ArrayList;

import rimidalv111.nichepulse.moz.MozLookup;
import rimidalv111.ypaa.main.HandleOutput;
import rimidalv111.ypaa.main.VideoInfo;
import rimidalv111.ypaa.main.YPAA;

import com.seomoz.api.authentication.Authenticator;
import com.seomoz.api.service.URLMetricsService;

public class ThreadsHandler extends Thread
{

	public YPAA instance;
	public ArrayList<VideoInfo> moz_que = new ArrayList<VideoInfo>();
	public MozLookup mozLookup;
	public ArrayList<VideoInfo> qued_keywords = new ArrayList<VideoInfo>();
	public ArrayList<VideoInfo> completed_keywords = new ArrayList<VideoInfo>();
	public HandleOutput handleOutput;
	public boolean running_search = false;
	public String accessID = "";
	public String secretKey = "";
	
	public ThreadsHandler(YPAA i, HandleOutput ho)
	{
		instance = i;
		handleOutput = ho;
	}

	public boolean keepAlive = true;
	
	public void runNextKeyword()
	{
			try
			{
				
				if(qued_keywords.size() > 0 && !running_search)
				{
					running_search = true;
					
					VideoInfo videoInfo = qued_keywords.get(0);
					
					Thread.sleep(4200);
					
					crawlPageForKeyword(videoInfo);
					
				}
				
			} catch (Exception io)
			{
				io.printStackTrace();
			}
	}
	
	public void crawlPageForKeyword(VideoInfo vi)
	{
		(new PageCrawler(this, vi)).start();
	}
	
	// MOZ STUFF -------------------------------------------
	private Authenticator moz_authenticator;
	private URLMetricsService urlMetricsService;

	public void setUpMoz()
	{
		moz_authenticator = new Authenticator();
		moz_authenticator.setAccessID(accessID);
		moz_authenticator.setSecretKey(secretKey);

		urlMetricsService = new URLMetricsService(moz_authenticator);
	}

	public void handle_completed_Keyword(VideoInfo vi)
	{
		completed_keywords.add(vi);
		handleOutput.addEntry(vi);
		
		//remove keyword from que
		int marked_for_removal = -1;
		
		for(int i = 0; i < qued_keywords.size(); i++)
		{
			if(qued_keywords.get(i).getVideo_Keyword().equalsIgnoreCase(vi.getVideo_Keyword()))
			{
				marked_for_removal = i;
			}
		}
		
		if(marked_for_removal >= 0)
		{
			qued_keywords.remove(marked_for_removal);
		}
		
		instance.updateProgressBar();
	}
	
	public Authenticator mozAuth()
	{
		return moz_authenticator;
	}

	public void setMoz_authenticator(Authenticator moz_authenticator)
	{
		this.moz_authenticator = moz_authenticator;
	}

	public MozLookup getMozLookup()
	{
		return mozLookup;
	}

	public void setMozLookup(MozLookup mozLookup)
	{
		this.mozLookup = mozLookup;
	}


	public ArrayList<VideoInfo> getMoz_que()
	{
		return moz_que;
	}

	public void setMoz_que(ArrayList<VideoInfo> moz_que)
	{
		this.moz_que = moz_que;
	}

	public URLMetricsService getUrlMetricsService()
	{
		return urlMetricsService;
	}

	public void setUrlMetricsService(URLMetricsService urlMetricsService)
	{
		this.urlMetricsService = urlMetricsService;
	}

	public YPAA getInstance()
    {
    	return instance;
    }

	public void setInstance(YPAA instance)
    {
    	this.instance = instance;
    }

	public ArrayList<VideoInfo> getQued_keywords()
    {
    	return qued_keywords;
    }

	public void setQued_keywords(ArrayList<VideoInfo> qued_keywords)
    {
    	this.qued_keywords = qued_keywords;
    }

	public String getAccessID()
    {
    	return accessID;
    }

	public void setAccessID(String accessID)
    {
    	this.accessID = accessID;
    }

	public String getSecretKey()
    {
    	return secretKey;
    }

	public void setSecretKey(String secretKey)
    {
    	this.secretKey = secretKey;
    }
}
