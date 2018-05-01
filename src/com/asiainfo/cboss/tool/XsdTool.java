/**
 * 
 */
package com.asiainfo.cboss.tool;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.exolab.castor.builder.SourceGenerator;

/**
 * @author zengxr
 * 2007-12-25 下午10:36:29
 * XsdTool
 */
public class XsdTool {
	static JFrame frame = new JFrame("XML desc 2 JAVA");
	
	static JLabel lblformat = new JLabel("                    ");

	static JLabel lblSaveDir = new JLabel("Save  Dir :");
	static JTextField txtSaveDir = new JTextField(System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "xsd", 40);
	
	static JTextArea ta = new JTextArea();
	static JScrollPane jsp = new JScrollPane(ta);

	static JLabel lblRootNode = new JLabel("Root Node :");
	static JTextField txtRootNode = new JTextField("", 40);

	static JLabel lblFileName = new JLabel("File Name :");
	static JTextField txtFileName = new JTextField("", 40);
	
	static JLabel lblPackageName = new JLabel("Package Name :");
	static JTextField txtPackageName = new JTextField("com.asiainfo.agent.xsd.", 40);
	static JLabel lblDestDir = new JLabel("Dest Dir :");
	static JTextField txtDestDir = new JTextField("", 40);
	
	static JButton btnSave = new JButton("Save Xsd File");
	static JButton btnParse = new JButton("Parse the content");
	static JButton btnXsdToJava = new JButton("XsdToJava");
	
	static String PROP_FILE= null;
	
	public static void startFrame() throws Exception{
		frame.getContentPane().setLayout(new BorderLayout());
		//CBossLogger.set
		JPanel ap = new JPanel();
		ap.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		ap.add(lblSaveDir, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		ap.add(txtSaveDir, gbc);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		ap.add(lblRootNode, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		ap.add(txtRootNode, gbc);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		ap.add(lblFileName, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		ap.add(txtFileName, gbc);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		ap.add(lblPackageName, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		ap.add(txtPackageName, gbc);

		gbc.gridwidth = 1;
		ap.add(lblDestDir, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		ap.add(txtDestDir, gbc);
		
		JPanel p1 = new JPanel();
		p1.add(btnParse);
		p1.add(lblformat);
		p1.add(btnSave);
		p1.add(btnXsdToJava);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		ap.add(p1, gbc);
		
		frame.getContentPane().add(ap, BorderLayout.NORTH);
		frame.getContentPane().add(jsp);
		
		//分析内容
		btnParse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				parse();
			}
		});
		
		btnSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		//xsd 2 java
		btnXsdToJava.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				xsdToJava();
			}
		});
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		
		//从配置文件读取上次保存的数据
		readPreConfig();
		
		ta.setText("输入格式：\n节点名称\t父节点名称\t约束\t类型(目录可空)\t长度(目录可空)\t描述(可空)\t备注说明(可空)");
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	/**
	 * 获取上次保存的数据
	 * @throws Exception
	 */
	private static void readPreConfig() throws Exception{
		File file = new File(PROP_FILE);
		if(file.isFile() == false){
			return;
		}
		
		//从配置文件获取配置数据
		FileInputStream inStream = new FileInputStream(file);
		
		Properties prop = new Properties();
		prop.load(inStream);
		
		//设置包名
		String value = prop.getProperty("txtPackageName");
		if(value != null){
			txtPackageName.setText(value);
		}
		
		//设置原文件路径
		value = prop.getProperty("txtDestDir");
		if(value != null){
			txtDestDir.setText(value);
		}
		inStream.close();
		
	}
	

	/**
	 * 将当期的配置数据写入文件
	 * @throws Exception
	 */
	private static void savePreConfig() throws Exception{
		Properties prop = new Properties();
		
		//设置包名
		prop.setProperty("txtPackageName" ,txtPackageName.getText());
		//设置原文件路径
		prop.setProperty("txtDestDir" ,txtDestDir.getText());
		
		//将配置数据写入文件
		FileOutputStream outputStrean = new FileOutputStream(PROP_FILE);
		prop.store(outputStrean, "xsd2java config data");
		outputStrean.close();
		
	}
	/**
	 * 将XSD生成JAVA代码
	 */
	public static void xsdToJava()  {
		//包名
		String pack = txtPackageName.getText();
		//源文件存放路径
	    String destDir=txtDestDir.getText();
	    
		String savaDir = txtSaveDir.getText();
		String savaFile = txtFileName.getText();
		String fileName=savaDir+"/"+savaFile+".xsd";
	    
		SourceGenerator sg = new SourceGenerator();
		try {
			//保存当期的配置数据
			savePreConfig();
			
			sg.setDestDir(destDir);
     		sg.generateSource(fileName, pack);
     		JOptionPane.showMessageDialog(null, "Xsd To Java Success !");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void save(){
		XsdNode[] nodes = parse();
		XsdCreator creator = new XsdCreator();
		String rootNode = txtRootNode.getText();
		if("".equals(rootNode) || rootNode == null){
			rootNode = nodes[0].getNodeName();
		}
		try{
			File f = new File(txtSaveDir.getText() + File.separator + txtFileName.getText() + ".xsd");
			if(f.exists()){
				if(JOptionPane.showConfirmDialog(null, "File exists, over write it ?") != JOptionPane.OK_OPTION){
					return;
				}
			}
			creator.createXsdFile(txtSaveDir.getText(), txtFileName.getText(), rootNode, nodes);
			JOptionPane.showMessageDialog(null, "Save File[" + txtSaveDir.getText() + File.separator + txtFileName.getText() + ".xsd" + "] Success !");
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Save Error !");
		}
	}
	
	//分析内容
	public static XsdNode[] parse() {
		String text = ta.getText();
		if(text == null || text.length() == 0) return null;
		BufferedReader br = new BufferedReader(new StringReader(text));
		try{
			String line = null;
			ArrayList al = new ArrayList();
			Map parents = new HashMap();
			String root = null;
			
			//读取一行
			while((line = br.readLine()) != null){
				String[] fields = line.split("\t");
				if((fields != null && fields.length > 2)){
//					if(fields[1].equals(root)){
//						txtFileName.setText(fields[2].trim());
//					}
					XsdNode node = new XsdNode();
					node.setNodeNo("1");
//					if(fields[0].indexOf(".") > -1){
//						String no = fields[0].trim();
//						String[] nosplits = no.split("\\.");
//						StringBuffer nodeNo = new StringBuffer();
//						for(int x = 0; x < nosplits.length; x ++){
//							nodeNo.append(nosplits[x].trim());
//							if(x < nosplits.length - 1){
//								nodeNo.append(".");
//							}
//						}
//						node.setNodeNo(nodeNo.toString());
//					}else{
//						node.setNodeNo(fields[0].trim());
//					}
					
					
					//节点名称、父节点名称、约束
					node.setNodeName(fields[0].trim());
					node.setPNodeName(fields[1].trim());
					node.setConstrain(fields[2].trim());
					
					//类型，对于目录，可空
					if(fields.length>3){
						node.setDataType(fields[3].trim());
					}else{
						node.setDataType("－");
					}
					
					//节点长度，对于目录可空
					if(fields.length>4){
						node.setLength(fields[4].trim());
					}
					else{
						node.setLength("－");
					}
					
					//描述
					if(fields.length > 5){
						node.setDescript(fields[5].trim());
					}
					
					//备注
					if(fields.length > 6){
						node.setNotes(fields[6].trim());
					}
					
					node.setFileNo(0);
					node.setFileName(txtFileName.getText());
					
					//第一行定义成ROOT
					if(root == null && (txtRootNode.getText() == null || txtRootNode.getText().length() == 0)) {
						root = node.getPNodeName();
						String nodeNo = "";
						if(node.getNodeNo().indexOf(".") > 0){
							nodeNo = node.getNodeNo().substring(0, node.getNodeNo().indexOf("."));
						}
						//txtRootNode.setText(root);
						txtFileName.setText(root);
						XsdNode parent = new XsdNode();
						parent.setConstrain("1");
						parent.setDataType("－");
						parent.setDescript("ROOT");
						parent.setNotes("ROOT");
						parent.setLength("－");
						parent.setNodeNo(nodeNo);
						parent.setNodeName(node.getPNodeName());
						parent.setPNodeName("ROOT");
						al.add(parent);
						//parents.put(node.getNodeNo().substring(node.getNodeNo().lastIndexOf(".")), parent);
					}else if(root == null && node.getNodeName().equals(txtRootNode.getText())){
						root = node.getNodeName();
						txtFileName.setText(root);
					}
					
					parents.put(node.getNodeNo(), node);
					al.add(node);
				}
			}
			
			XsdNode[] nodes = (XsdNode[])al.toArray(new XsdNode[]{});
//			Arrays.sort(nodes);
//			for(int i = 1; i < nodes.length; i ++){
//				if(nodes[i].getPNodeName().equals(root)) {
//					continue;
//				}
//				try{
//					System.out.println(nodes[i].getNodeNo().substring(0, nodes[i].getNodeNo().lastIndexOf(".")));
//				}catch(Exception ex){
//					ex.printStackTrace();
//				}
//				XsdNode pnode = (XsdNode)parents.get(nodes[i].getNodeNo().substring(0, nodes[i].getNodeNo().lastIndexOf(".")));
//				if(pnode == null || !pnode.getNodeName().equals(nodes[i].getPNodeName())){
//					JOptionPane.showMessageDialog(null, "Node [" + nodes[i].getNodeName() + "] have no Parent node!");
//					break;
//				}
//			}
//			
			return nodes;
		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error : " + ex.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args)throws Exception{
		PROP_FILE = args[0];
		startFrame();
	}

}
