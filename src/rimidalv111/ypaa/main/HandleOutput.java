package rimidalv111.ypaa.main;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HandleOutput extends JFrame
{
    private static final long serialVersionUID = 1L;
	public JFrame outputFrame;
	public JEditorPane outputPanel;
	
	StringBuilder top_html = new StringBuilder().append("<html>").append("  <head>").append("    <title></title>").append("  </head>").append("  <body>").append("	<!-- ffd6d6 -->").append("	<hr>");

	StringBuilder middle_html = new StringBuilder().append("<div height=\"auto\" style=\"text-align: center; background-color: #e6ffd6\">").append("		<div style=\"float: clear; text-align: left; margin: 20px;\">").append("			<p>").append("			Keyword:  <b><a href=\"https://www.google.com/search?q=[KEYWORD]\">[KEYWORD]</a></b>").append("			</p>		").append("			<p>").append("			Title:  <b>[title]</b>").append("			</p>").append("			<p>").append("				Link:  <b><a href=\"[url]\">[url]</a></b>").append("				<br>").append("				Page Authority:  <b>[pageauthority]</b>").append("			</p>").append("		</div>").append("	</div>").append("	<hr>");

	StringBuilder bottom_html = new StringBuilder().append("</div></body>").append("</html>");

	ArrayList<String> middle_entries = new ArrayList<String>();

	StringBuilder unknown_keywords = new StringBuilder().append("<div height=\"auto\" style=\"text-align: left; background-color: #fff7d6\">").append("		<b>No Video Results For Keywords...</b><br>-----<br></p>");

	ArrayList<String> notfound_entries = new ArrayList<String>();

	public String current_Document = "";
	
	public HandleOutput(YPAA th)
	{

		outputPanel = new JEditorPane();

		outputPanel.setEditable(false);
		outputPanel.setContentType("text/html");

		JScrollPane scrollPane = new JScrollPane(outputPanel);

		outputFrame = new JFrame("Results");
		outputFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		URL iconURL = getClass().getResource("favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		outputFrame.setIconImage(icon.getImage());
		
		outputFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				//close only frame on exit
			}
		});

		// display the frame
		outputFrame.setSize(new Dimension(600, 500));
		outputFrame.setLocationRelativeTo(null);
	}

	public void update_html()
	{
		String html = top_html.toString();

		for(String entry_html : middle_entries)
		{
			html = html + entry_html;
		}

		html = html + unknown_keywords;
		
		for(String notfound : notfound_entries)
		{
			html = html + notfound;
			
		}
		
		html = html + bottom_html;

		current_Document = html;
		
		outputPanel.setText(html);
		outputPanel.validate();
		outputPanel.repaint();
		outputFrame.validate();
		outputFrame.repaint();
	}

	public void addEntry(VideoInfo vi)
	{

		if(vi.getVideo_title() == null || vi.getPage_authority() == -1 || vi.getVideo_url() == null)//no videos on page
		{
			String notfound_entry = vi.getVideo_Keyword() + "<br>";
			notfound_entries.add(notfound_entry);
			System.out.println("No Video Data for: " + vi.getVideo_title() + " " + vi.getPage_authority() + " " + vi.getVideo_url());
			update_html();
			return;
		}
		
		String entry = middle_html.toString();
		
		if(vi.getPage_authority() > 5) //if main pa is over 5 then make it red entry
		{
			entry = entry.replace("#e6ffd6", "#ffd6d6");
		}

		entry = entry.replace("[KEYWORD]", vi.getVideo_Keyword());

		entry = entry.replace("[title]", vi.getVideo_title());

		entry = entry.replace("[url]", vi.getVideo_url());

		entry = entry.replace("[pageauthority]", vi.getPage_authority() + " " + vi.getExtra_pa());

		middle_entries.add(entry);

		update_html();
	}

	public JFrame getOutputFrame()
    {
    	return outputFrame;
    }

	public void setOutputFrame(JFrame outputFrame)
    {
    	this.outputFrame = outputFrame;
    }
}
