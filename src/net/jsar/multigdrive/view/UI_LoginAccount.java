package net.jsar.multigdrive.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JTextField;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import net.jsar.multigdrive.mvc.ViewManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI_LoginAccount extends UI_GeneralView {

	protected static final String MSG_ACCOUNTID = "Enter here a name for the account...";
	protected static final String MSG_LOCALFOLDER = "Choose a local folder...";

	protected JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		createAndShow();
	}
	
	public static void createAndShow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI_LoginAccount frame = new UI_LoginAccount();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UI_LoginAccount() {
		super();
		setResizable(false);
		initUI();
	}
	
	public void initUI() {			
		setTitle("Login New Account");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		setBounds(100, 100, 550, 300);
		setLocationRelativeTo(null);
				
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {130, 360, 0};
		gbl_contentPane.rowHeights = new int[] {30, 30, 30, 30, 0};
		gbl_contentPane.columnWeights = new double[]{0.0};
		gbl_contentPane.rowWeights = new double[]{0.0};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblAccountName = new JLabel("Account Name:  ");
		lblAccountName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAccountName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblAccountName = new GridBagConstraints();
		gbc_lblAccountName.anchor = GridBagConstraints.EAST;
		gbc_lblAccountName.fill = GridBagConstraints.VERTICAL;
		gbc_lblAccountName.insets = new Insets(0, 0, 5, 5);
		gbc_lblAccountName.gridx = 0;
		gbc_lblAccountName.gridy = 0;
		contentPane.add(lblAccountName, gbc_lblAccountName);
		
		final JTextField field_accountId = new JTextField(MSG_ACCOUNTID);
		field_accountId.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				if (field_accountId.getText().equals(MSG_ACCOUNTID)) field_accountId.setText("");
			}
		});
		field_accountId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_field_accountId = new GridBagConstraints();
		gbc_field_accountId.fill = GridBagConstraints.HORIZONTAL;
		gbc_field_accountId.gridx = 1;
		gbc_field_accountId.gridy = 0;
		contentPane.add(field_accountId, gbc_field_accountId);
				
		
		final JTextField local_folder = new JTextField(MSG_LOCALFOLDER);
		//local_folder.addMouseListener(new MouseAdapter() {
		//	public void mouseClicked(MouseEvent arg0) {
		//		if (local_folder.getText().equals(MSG_LOCALFOLDER)) local_folder.setText("");
		//	}
		//});
		local_folder.setEditable(false);
		local_folder.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_local_folder = new GridBagConstraints();
		gbc_local_folder.fill = GridBagConstraints.HORIZONTAL;
		gbc_local_folder.gridx = 1;
		gbc_local_folder.gridy = 1;
		contentPane.add(local_folder, gbc_local_folder);
		
		JButton btnLocalFolder = new JButton("Local Folder...");
		btnLocalFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = (new UI_FileChooser(JFileChooser.DIRECTORIES_ONLY)).selectFile();
				if (file != null) local_folder.setText(file.toString());
			}
		});
		btnLocalFolder.setHorizontalAlignment(SwingConstants.RIGHT);
		btnLocalFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnLocalFolder = new GridBagConstraints();
		gbc_btnLocalFolder.anchor = GridBagConstraints.EAST;
		gbc_btnLocalFolder.fill = GridBagConstraints.VERTICAL;
		gbc_btnLocalFolder.insets = new Insets(0, 0, 5, 5);
		gbc_btnLocalFolder.gridx = 0;
		gbc_btnLocalFolder.gridy = 1;
		contentPane.add(btnLocalFolder, gbc_btnLocalFolder);
				
		JPanel btnPanel = new JPanel();
		GridBagConstraints gbc_btnPanel = new GridBagConstraints();
		//gbc_btnPanel.anchor = GridBagConstraints.EAST;
		//gbc_btnPanel.fill = GridBagConstraints.VERTICAL;
		gbc_btnPanel.insets = new Insets(0, 0, 5, 5);
		gbc_btnPanel.gridx = 1;
		gbc_btnPanel.gridy = 4;
		contentPane.add(btnPanel, gbc_btnPanel);
		GridBagLayout gbl_btnPanel = new GridBagLayout();
		gbl_btnPanel.columnWidths = new int[] {80, 40, 80, 0};
		gbl_btnPanel.rowHeights = new int[] {30, 0};
		gbl_btnPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_btnPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		btnPanel.setLayout(gbl_btnPanel);
		
		JButton btnNewButton_1 = new JButton("OK");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String accountId = field_accountId.getText().replace(MSG_ACCOUNTID,"");
				if (accountId == null || accountId.length() == 0) {					
					showAlert("A name for the account must be specified");
					return;
				}
				
				String localFolder = local_folder.getText().replace(MSG_LOCALFOLDER,"");
				if (localFolder == null || localFolder.length() == 0) {					
					showAlert("A local folder for the account must be specified");
					return;
				}				
				close();
				//viewManager.notifyLoginNewAccount(accountId, local_folder.getText());
				ViewManager.INSTANCE.notifyLoginAccount(accountId, local_folder.getText());
				
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		btnPanel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton = new JButton("Cancel");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		btnPanel.add(btnNewButton, gbc_btnNewButton);
		
	}

}
