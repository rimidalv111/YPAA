package rimidalv111.ypaa.main;

import java.util.ArrayList;

public class VideoInfo
{
	private String video_url = null;
	private String video_title = null;
	private String video_img_url = null;
	private Integer page_authority = null;
	private String video_Keyword = null;
	private ArrayList<String> extra_urls = new ArrayList<String>();
	private ArrayList<String> extra_pa = new ArrayList<String>();

	public String getVideo_url()
	{
		return video_url;
	}

	public void setVideo_url(String video_url)
	{
		this.video_url = video_url;
	}

	public String getVideo_title()
	{
		return video_title;
	}

	public void setVideo_title(String video_title)
	{
		this.video_title = video_title;
	}

	public String getVideo_img_url()
	{
		return video_img_url;
	}

	public void setVideo_img_url(String video_img_url)
	{
		this.video_img_url = video_img_url;
	}

	public Integer getPage_authority()
	{
		return page_authority;
	}

	public void setPage_authority(Integer page_authority)
	{
		this.page_authority = page_authority;
	}

	public String getVideo_Keyword()
	{
		return video_Keyword;
	}

	public void setVideo_Keyword(String video_Keyword)
	{
		this.video_Keyword = video_Keyword;
	}

	public ArrayList<String> getExtra_urls()
	{
		return extra_urls;
	}

	public void setExtra_urls(ArrayList<String> extra_urls)
	{
		this.extra_urls = extra_urls;
	}

	public ArrayList<String> getExtra_pa()
	{
		return extra_pa;
	}

	public void setExtra_pa(ArrayList<String> extra_pa)
	{
		this.extra_pa = extra_pa;
	}

}
