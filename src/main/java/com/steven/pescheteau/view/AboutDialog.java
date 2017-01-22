package com.steven.pescheteau.view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.steven.pescheteau.model.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AboutDialog extends JDialog {

	private Logger LOG = LoggerFactory.getLogger(AboutDialog.class);

	public AboutDialog() {
		super();
		setSize(new Dimension(230, 220));
		setLocationRelativeTo(null);
		setAlwaysOnTop(false);
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Suppliers Owens Corning - About");
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JButton button = new JButton("       OK        ");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { dispose(); }
		});

		JPanel ok = new JPanel();
		ok.add(button);

		panel.add(new JLabel("Suppliers Owens Corning - version " + Settings.VERSION), BorderLayout.NORTH);

		JButton img = new JButton(new ImageIcon(getClass().getResource("/com/steven/pescheteau/images/profil.png")));
		img.setPreferredSize(new Dimension(105, 105));
		img.setFocusPainted(false);
		img.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
					try {
						desktop.browse(new URI("https://fr.linkedin.com/in/stevenpescheteau"));
					} catch (Exception e1) {
                        LOG.error(e1.getMessage());
                    }
			}
		});
		JPanel panelImg = new JPanel();
		panelImg.add(img);
		panel.add(panelImg, BorderLayout.CENTER);
		panel.add(ok, BorderLayout.SOUTH);
		getContentPane().add(panel);
	}
}