/****************************************
 * URLProber
 * A basic URL probing device, useful
 * for barraging a server with GET
 * requests. :P
 * --------------------------------------
 * Princeton Ferro - Sept. 13, 2012
*****************************************/
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.swing.table.*;

import java.util.*;

public class URLProber {
	public static int trialCount = 0;
	public static DefaultTableModel model = new DefaultTableModel(){
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	public static String jtextname = "";
	public static JPanel txt = new JPanel(){
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawString(jtextname,0,20);
		}
	};
	public static void main(String[] args) {
		Process daemon = new Process();
		daemon.setDaemon(true);
		daemon.start();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Toolkit tk = Toolkit.getDefaultToolkit();
				Dimension scrn = tk.getScreenSize();
				Dimension winloc = new Dimension((scrn.width-800)/2,(scrn.height-450)/2);
				JFrame window = new JFrame("URL Prober");
				window.setSize(800,450);
				window.setResizable(false);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit
				window.setLocation(winloc.width,winloc.height);
				window.setVisible(true);
				JPanel pn = new JPanel();
				pn.setLayout(null);
				final JDialog dlg = new JDialog(window, "About", Dialog.ModalityType.APPLICATION_MODAL);
				dlg.setSize(400,200);
				dlg.setLocation(winloc.width+(800-400)/2,winloc.height+(450-200)/2);
				dlg.setVisible(false);
				JPanel dlgtxt = new JPanel(){
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						Graphics2D g2d = (Graphics2D)g;
						g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						int strlen = (int) g2d.getFontMetrics().getStringBounds("URL Prober", g2d).getWidth();
						g2d.drawString("URL Prober",(400-strlen)/2,20);
						strlen = (int) g2d.getFontMetrics().getStringBounds("Version 1.0", g2d).getWidth();
						g2d.drawString("Version 1.01",(400-strlen)/2,40);
						strlen = (int) g2d.getFontMetrics().getStringBounds("2012 Princeton Ferro", g2d).getWidth();
						g2d.drawString("2012 Princeton Ferro",(400-strlen)/2,60);
						String descr = "This program is free to modification and distribution";
						String descr2 = "without the permission of its author.";
						strlen = (int) g2d.getFontMetrics().getStringBounds(descr, g2d).getWidth();
						g2d.drawString(descr,(400-strlen)/2,100);
						strlen = (int) g2d.getFontMetrics().getStringBounds(descr2, g2d).getWidth();
						g2d.drawString(descr2,(400-strlen)/2,120);
					}
				};
				dlgtxt.setBounds(50,0,400,300);
				dlg.add(dlgtxt);
				model.addColumn("Trial");
				model.addColumn("Location (URL)");
				model.addColumn("Status");
				JTable tbl = new JTable(model);
				tbl.getColumnModel().getColumn(0).setPreferredWidth(50);
				tbl.getColumnModel().getColumn(1).setPreferredWidth(650);
				tbl.getColumnModel().getColumn(2).setPreferredWidth(100);
				tbl.setBounds(0,0,300,300);
				JScrollPane spn = new JScrollPane(tbl);
				spn.setBounds((800-768)/2,(400-350)/3,768,350);
				pn.add(spn);
				JButton bt = new JButton("About");
				bt.setBounds(705,375,80,30);
				bt.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dlg.setVisible(true);
					}
				});
				pn.add(bt);
				JButton clbt = new JButton("Clear");
				clbt.setBounds(615,375,80,30);
				clbt.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						model.setRowCount(0);
					}
				});
				pn.add(clbt);
				txt.setBounds(15,375,580,30);
				pn.add(txt);
				window.add(pn);
			}
		});
	}
	public static class Process extends Thread {
		@Override
			public void run() {
				while (true) {
					if (trialCount!=0)
						try {
							vote(6535313, 29577660, 0, 10); //sends vote
						} catch (Exception e) {
							e.printStackTrace();
							System.exit(1);
						}
					trialCount++;
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
	}
	public static void vote(int id, int answer, int poll_type, int options) throws Exception {
		Random rg = new Random();
		int rand = rg.nextInt(100);
		boolean successful = false;
		String urlloc = "http://answers.polldaddy.com/vote/?va=10&pt=0&r=1&p="+id+"&a="+answer+"&o=";
		URL loc = new URL(urlloc);
		updateGUIStatus("Trying to connect to "+urlloc+"...");
		HttpURLConnection con = (HttpURLConnection)loc.openConnection();
		con.setRequestMethod("GET");
		con.connect();
		int resp = con.getResponseCode();
		if (resp == 200) {
			updateGUIStatus("Successfully connected to server, and voted.");
			successful = true;
		} else updateGUIStatus("Undesired connection result. Response was code "+resp+".");
		model.addRow(new Object[]{trialCount, urlloc, (successful?"Successful":"Failed")});
	}
	public static void updateGUIStatus(String txt1) {
		jtextname = txt1;
		txt.repaint();
	}
}