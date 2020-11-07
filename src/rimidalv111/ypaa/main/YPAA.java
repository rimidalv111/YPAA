package rimidalv111.ypaa.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import rimidalv111.nichepulse.threads.ThreadsHandler;

public class YPAA extends JFrame
{
    private static final long serialVersionUID = 1L;
	private YPAA instance;
	private JPanel main_Pane;
	private ThreadsHandler threadsHandler;
	public HandleOutput handleOutput = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					YPAA frame = new YPAA();
					frame.setVisible(true);
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	JScrollPane keyword_scrollPane;
	JEditorPane keyword_editorPane;
	private JLabel keywords_label_top;
	private JButton btn_Analyze = new JButton("Analyze");
	private JButton btn_save = new JButton("Save");
	public JProgressBar progressBar;
	public String ca = "http://fordsimr.com";
	public YPAA()
	{

		instance = this;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 375, 485);
		setResizable(false);
		URL iconURL = getClass().getResource("favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());
		
		main_Pane = new JPanel();
		main_Pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(main_Pane);
		main_Pane.setLayout(null);

		JPanel keyword_panel = new JPanel();
		keyword_panel.setBounds(10, 52, 340, 244);
		main_Pane.add(keyword_panel);
		keyword_panel.setLayout(new BorderLayout(0, 0));

		keyword_scrollPane = new JScrollPane();
		keyword_panel.add(keyword_scrollPane);
		
				keyword_editorPane = new JEditorPane();
				keyword_scrollPane.setViewportView(keyword_editorPane);

		keywords_label_top = new JLabel();
		keywords_label_top.setText("Enter Keywords:");
		keywords_label_top.setBounds(10, 27, 242, 14);
		main_Pane.add(keywords_label_top);
		btn_Analyze.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(initialProgressBarSize == 0)
				{
					if(moz_accessID.getText().isEmpty() || moz_secretKey.getText().isEmpty())
					{
						keywords_label_top.setText("Input Moz Info & Click --->");
						return;
					} else
					{
						if(threadsHandler.getAccessID().isEmpty() && threadsHandler.getSecretKey().isEmpty())
						{
							threadsHandler.setAccessID(moz_accessID.getText());
							threadsHandler.setSecretKey(moz_secretKey.getText());
							threadsHandler.setUpMoz();
							keywords_label_top.setText("Enter Keywords:");
							keywords_label_top.validate();
							keywords_label_top.repaint();
						}
					}
					if(keyword_editorPane.getText() != null)
					{
						handle_keywords();
					}
					if(!threadsHandler.handleOutput.getOutputFrame().isVisible())
					{
						threadsHandler.handleOutput.getOutputFrame().setVisible(true);
					}
				}
			}
		});
		btn_Analyze.setBounds(209, 12, 142, 29);
		main_Pane.add(btn_Analyze);

		btn_save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(btn_save.getText().contains("Continue"))
				{
					continueAnalyze();
				} else
				{
					saveOutput();
				}
			}
		});
		btn_save.setBounds(10, 412, 340, 30);
		main_Pane.add(btn_save);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 370, 340, 31);
		main_Pane.add(progressBar);
		
		moz_accessID = new JTextField();
		moz_accessID.setBounds(209, 307, 141, 20);
		main_Pane.add(moz_accessID);
		moz_accessID.setColumns(10);
		
		moz_secretKey = new JTextField();
		moz_secretKey.setBounds(209, 339, 141, 20);
		main_Pane.add(moz_secretKey);
		moz_secretKey.setColumns(10);
		
		JLabel lblAccessId = new JLabel("Access ID:");
		lblAccessId.setBounds(127, 310, 72, 14);
		main_Pane.add(lblAccessId);
		
		JLabel lblSecretKey = new JLabel("Secret Key:");
		lblSecretKey.setBounds(127, 342, 72, 14);
		main_Pane.add(lblSecretKey);

		handleOutput = new HandleOutput(instance);

		//initiate stuff
		threadsHandler = (new ThreadsHandler(instance, handleOutput));
		threadsHandler.start();

		handle();
	}

	public void handle()
	{
		String i  = WinHw.getSerialNumber();
		System.out.println(i);
		try
		{
			Document doc = Jsoup.connect(dpr3 + i).userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)").timeout(5000).get();
			setUp(doc.text());
		} catch(Exception io)
		{
			System.exit(0);
		}

	}
	
	public void setUp(String keywords)
	{
		if(keywords.startsWith("[N]"))
		{
			setMainTitle(keywords);
		} else
		{
			System.exit(0);
		}
	}
	
	public void setMainTitle(String title)
	{
		setTitle("YPAA - " + title);
	}
	
	public void handle_keywords()
	{
		String s[] = keyword_editorPane.getText().split("\\r?\\n");
		ArrayList<String> arrList = new ArrayList<>(Arrays.asList(s));
		for(String kw : arrList)
		{
			VideoInfo videoInfo = new VideoInfo();
			videoInfo.setVideo_Keyword(kw);
			threadsHandler.qued_keywords.add(videoInfo);
		}
		if(getTitle().isEmpty())
		{
			System.exit(0);
		}
		setUpProgressBar();
		threadsHandler.runNextKeyword();
	}

	public int initialProgressBarSize = 0;
	private JTextField moz_accessID;
	private JTextField moz_secretKey;

	public void setUpProgressBar()
	{
		progressBar.setMinimum(0);
		progressBar.setMaximum(threadsHandler.qued_keywords.size());
	}

	private String conp2 = ca + "/checker/cna";
	
	public void updateProgressBar()
	{
		progressBar.setValue(threadsHandler.qued_keywords.size());
	}

	public void changeIP()
	{
		System.out.println("Your IP is temp banned for an hour... time to change your IP address!");

		saveOutput();
		keywords_label_top.setText("Please Change IP & Hit 'Continue'");
		btn_save.setText("Continue ... " + threadsHandler.qued_keywords.size() + " Keywords Left");

		keywords_label_top.validate();
		keywords_label_top.repaint();
		btn_save.validate();
		btn_save.repaint();

		setUpProgressBar();
		updateProgressBar();
	}

	public void continueAnalyze()
	{
		keywords_label_top.setText("Enter Keywords:");
		btn_save.setText("Save");

		keywords_label_top.validate();
		keywords_label_top.repaint();
		btn_save.validate();
		btn_save.repaint();

		threadsHandler.runNextKeyword();

		System.out.println("Continuing...");
	}

	private String dpr3 = conp2 + "/v.php?a=";
	
	public void saveOutput()
	{
		if(getTitle().isEmpty())
		{
			System.exit(0);
		}
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriter((new File(".")).getAbsolutePath() + "/YPAA_Results.html"));

			writer.write(threadsHandler.handleOutput.current_Document);

			writer.flush();
			writer.close();
		} catch(IOException e1)
		{
			e1.printStackTrace();
		}
	}

	public ThreadsHandler getThreadsHandler()
	{
		return threadsHandler;
	}

	public void setThreadsHandler(ThreadsHandler threadsHandler)
	{
		this.threadsHandler = threadsHandler;
	}

	public void updateOutput(VideoInfo vi)
	{
		handleOutput.addEntry(vi);
	}

	public HandleOutput getHandleOutput()
	{
		return handleOutput;
	}

	public void setHandleOutput(HandleOutput handleOutput)
	{
		this.handleOutput = handleOutput;
	}

	public JLabel getKeywords_label_top()
	{
		return keywords_label_top;
	}

	public void setKeywords_label_top(JLabel keywords_label_top)
	{
		this.keywords_label_top = keywords_label_top;
	}
}
