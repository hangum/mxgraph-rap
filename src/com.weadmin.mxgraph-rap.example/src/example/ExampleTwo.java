package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import com.weadmin.mxgraph_rap.GraphJS;
import com.weadmin.mxgraph_rap.MxGraphJS.MxGraphEvent;

public class ExampleTwo extends AbstractEntryPoint{

	private static final long serialVersionUID = 1L;

	String style1 = "shape=mxgraph.cisco.switches.multi-fabric_server_switch;html=1;dashed=0;fillColor=#036897;strokeColor=#ffffff;strokeWidth=2;verticalLabelPosition=bottom;verticalAlign=top";
	String style2 = "shape=mxgraph.cisco.switches.multi-fabric_server_switch;html=1;dashed=0;fillColor=#036897;strokeColor=#ffff00;strokeWidth=2;verticalLabelPosition=bottom;verticalAlign=top";
	String style3 = "shape=mxgraph.cisco.switches.multi-fabric_server_switch;html=1;dashed=0;fillColor=#036897;strokeColor=#ff0000;strokeWidth=2;verticalLabelPosition=bottom;verticalAlign=top";
	String style4 = "strokeColor=#228B22;dashed=0;startArrow=classic;targetPerimeterSpacing=-6;sourcePerimeterSpacing=-6;";
	String style5 = "shape=image;html=1;verticalLabelPosition=bottom;labelBackgroundColor=none;verticalAlign=top;imageAspect=1;aspect=fixed;image=rwt-resources/graph/images/application.png;strokeColor=#000000;fillColor=#FFFFFF;align=center;resourceLevel=3;";
	String style6 = "shape=image;html=1;verticalLabelPosition=bottom;labelBackgroundColor=none;verticalAlign=top;imageAspect=1;aspect=fixed;image=rwt-resources/graph/images/server.png;strokeColor=#000000;fillColor=#FFFFFF;align=center;resourceLevel=2;";
	String style7 = "text;html=1;resizable=0;points=[];align=center;verticalAlign=middle;labelBackgroundColor=none;rotation=45;";

	private Label hoverText;
	private Display display;
	private String filename;
	Label title;
	String lastids;
	ArrayList<String> ids;
	ArrayList<String> edgeids;
	ArrayList<String> testxml;

	private String getId(){
		return UUID.randomUUID().toString();
	}

	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new FillLayout());
		display = Display.getCurrent();
		testxml = new ArrayList<String>();
		//testxml.add("4");
//		testxml.add("1e4f7ee7-fffb-46c7-96a4-f1e52158d9f8");
//		testxml.add("744860a9-a67e-4054-96c0-92462c7448fa");
//		testxml.add("42c1a055-82a1-48ae-bda9-fb50866b4ad6");
//		testxml.add("0f14d356-ade5-432f-bea6-e6d1e9d92200");
//		testxml.add("c3ac924b-a175-473c-818c-6c48d93ecd87");
//		testxml.add("6");
		StartupParameters service = RWT.getClient().getService(StartupParameters.class);
		filename = service.getParameter("filename");

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns( 1 ).margins( 0, 0 ).applyTo( composite );

		Composite one = new Composite(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns( 13 ).extendedMargins(10, 0, 10, 5).applyTo( one );
		GridDataFactory.fillDefaults().align( SWT.FILL, SWT.FILL ).grab( true, false ).applyTo( one );

		Composite two = new Composite(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns( 9 ).margins( 0, 0 ).applyTo( two );
		GridDataFactory.fillDefaults().align( SWT.FILL, SWT.FILL ).grab( true, true ).applyTo( two );

		GraphJS g = new GraphJS(two, SWT.BORDER);
		//g.setBounds(20, 30, 800, 600);
	    GridDataFactory.fillDefaults().align( SWT.FILL, SWT.FILL ).span(9, 1).grab( true, true ).applyTo( g );

	    Button create = new Button(one, SWT.PUSH);
		create.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		create.setText("新建");
		create.setBackground(new Color(create.getDisplay(), 35, 130, 114));
		create.setForeground(create.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		create.setFont(new Font(create.getDisplay(), "楷体",17,SWT.NORMAL));
		create.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;
			@Override
			public void widgetSelected(SelectionEvent e) {
				JavaScriptExecutor executor = RWT.getClient().getService(JavaScriptExecutor.class);
				executor.execute("window.location.href='http://localhost:10010/hello2'");
			}
		});

	    Combo layout = new Combo(one, SWT.DROP_DOWN);
		layout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		layout.setItems(new String[]{"树型","圆型","堆型","随意","分层类型"});
		layout.setText("选择布局");
		layout.addSelectionListener(new SelectionListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = layout.getSelectionIndex();
				switch (index) {
				case 0:
					g.graphLayout("tree");
					break;
				case 1:
					g.graphLayout("circle");
					break;
				case 2:
					g.graphLayout("stack");
					break;
				case 3:
					g.graphLayout("fast");
					break;
				case 4:
					g.graphLayout("hierarchical");
					break;
				case 5:
					g.graphLayout("partition");
					break;
				default:
					break;
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Button zoomIn = new Button(one, SWT.PUSH);
		zoomIn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		zoomIn.setText("放大");
		zoomIn.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				g.zoomIn();
			}
		});

		Button zoomOut = new Button(one, SWT.PUSH);
		zoomOut.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		zoomOut.setText("缩小");
		zoomOut.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				g.zoomOut();
			}
		});

		Button zoomActual = new Button(one, SWT.PUSH);
		zoomActual.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		zoomActual.setText("还原");
		zoomActual.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				g.resetView();
			}
		});


		Button showArea = new Button(one, SWT.PUSH);
		showArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		showArea.setText("隐藏筛选器");
		showArea.setData("show", true);
		showArea.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean area = (boolean) showArea.getData("show");
				if (area) {
					showArea.setText("显示筛选器");
					showArea.setData("show", false);
					g.setControlarea("none");
				}else{
					showArea.setText("隐藏筛选器");
					showArea.setData("show", true);
					g.setControlarea("block");
				}
			}
		});

		Button small = new Button(one, SWT.PUSH);
		small.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		small.setText("缩略图");
		small.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;
			@Override
			public void widgetSelected(SelectionEvent e) {
				JavaScriptExecutor executor = RWT.getClient().getService(
				JavaScriptExecutor.class);
				executor.execute("window.location.href='http://localhost:10010/small'");
			}
		});

		Combo arrow = new Combo(one, SWT.DROP_DOWN);
		arrow.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		arrow.setItems(new String[]{"出箭头","入箭头","总箭头"});
		arrow.setText("箭头");
		arrow.addSelectionListener(new SelectionListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = arrow.getSelectionIndex();
				switch (index) {
				case 0:
					//g.arrowVisible(new String[]{"3","2"}, "out");
					JsonArray array = new JsonArray();
					JsonObject json = new JsonObject();
					JsonObject json2 = new JsonObject();
					json2.set("image", "rwt-resources/graph/images/error.png");
					json2.set("width", 16);
					json2.set("height", 16);
					json2.set("tooltip", "error");
					json.set("id", "3");
					json.set("tooltip", "<h1>abcd</h1>"+ "<img src='rwt-resources/graph/images/error.png"+"'/>");
					json.set("overlay", json2);
					array.add(json);
					g.updateNodeStatus(array);
					break;
				case 1:
					//g.arrowVisible(new String[]{"3","2"}, "in");
					JsonArray array1 = new JsonArray();
					JsonObject json1 = new JsonObject();
					JsonObject json21 = new JsonObject();
					json21.set("image", "rwt-resources/graph/images/error.png");
					json21.set("width", 16);
					json21.set("height", 16);
					json21.set("tooltip", "error");
					json1.set("id", "2");
					json1.set("tooltip", "<h1>abcd</h1>"+ "<img src='rwt-resources/graph/images/error.png"+"'/>");
					json1.set("overlay", json21);
					array1.add(json1);
					g.updateNodeStatus(array1);
					break;
				case 2:
					g.arrowVisible(new String[]{"3","2"}, "both");
					break;
				default:
					break;
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});


		Combo combo = new Combo(one, SWT.DROP_DOWN);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		combo.setItems(new String[]{"红实","紫实","黑虚"});
		combo.setText("状态");
		combo.addSelectionListener(new SelectionListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combo.getSelectionIndex()==0) {
					JsonArray array1 = new JsonArray();
					JsonObject json = new JsonObject();
					json.set("id", "4");
					json.set("color", "#FF3030");
					json.set("value", "1000Kbps");
					json.set("arrow", "0");
					json.set("dashed", "0");
					json.set("tooltip", "<h1>Ball</h1>");
					array1.add(json);
					if (edgeids!=null&&edgeids.size()>0) {
						for(String lastid : edgeids){
							JsonObject json2 = new JsonObject();
							json2.set("id", lastid);
							json2.set("color", "#FF3030");
							json2.set("value", "2000Kbps");
							json2.set("arrow", "0");
							json2.set("dashed", "0");
							json2.set("tooltip", "<h1>Balls</h1>");
							array1.add(json2);
						}
					}
					g.updateEdgeStatus(array1);
				}if (combo.getSelectionIndex()==1) {
					JsonArray array1 = new JsonArray();
					JsonObject json = new JsonObject();
					json.set("id", "4");
					json.set("color", "#D02090");
					json.set("value", "1000Kbps");
					json.set("arrow", "0");
					json.set("dashed", "0");
					array1.add(json);
					if (edgeids!=null&&edgeids.size()>0) {
						for(String lastid : edgeids){
							JsonObject json2 = new JsonObject();
							json2.set("id", lastid);
							json2.set("color", "#D02090");
							json2.set("value", "2000Kbps");
							json2.set("arrow", "0");
							json2.set("dashed", "0");
							array1.add(json2);
						}
					}
					g.updateEdgeStatus(array1);
				}if (combo.getSelectionIndex()==2) {
					JsonArray array1 = new JsonArray();
					JsonObject json = new JsonObject();
					json.set("id", "4");
					json.set("color", "#000000");
					json.set("value", "1000Kbps");
					json.set("arrow", "0");
					json.set("dashed", "1");
					array1.add(json);
					if (edgeids!=null&&edgeids.size()>0) {
						for(String lastid : edgeids){
							JsonObject json2 = new JsonObject();
							json2.set("id", lastid);
							json2.set("color", "#000000");
							json2.set("value", "2000Kbps");
							json2.set("arrow", "0");
							json2.set("dashed", "1");
							array1.add(json2);
						}
					}
					g.updateEdgeStatus(array1);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Button addChild = new Button(one, SWT.PUSH);
		addChild.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		addChild.setText("添加文字");
		addChild.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JsonObject json = new JsonObject();
				json.set("id", "4");
				json.set("end", "world!");
				json.set("start", "hello");
				g.addChilds(json);
			}
		});


		Button finish = new Button(one, SWT.PUSH);
		finish.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		finish.setText("完成");
		finish.setBackground(finish.getDisplay().getSystemColor(SWT.COLOR_DARK_CYAN));
		finish.setForeground(finish.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		finish.addSelectionListener(new SelectionAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (filename==null||filename.equals("")) {
					filename = getId()+".xml";
				}
				g.setModelXml(filename);
				JavaScriptExecutor executor = RWT.getClient().getService(JavaScriptExecutor.class);
				executor.execute("window.location.href='http://localhost:10010/small'");
			}
		});

		hoverText = new Label(g, SWT.BORDER);
		hoverText.setVisible(false);
		hoverText.setSize(100, 40);

		hoverText.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		mxGraph gd = new mxGraph();
		Object parentG = gd.getDefaultParent();
		g.setGraph(gd);
		try{
			if (filename!=null) {
				InputStream in = new FileInputStream(new File("D:/mxgraph/"+filename));
				g.loadGrapXml(mxUtils.readInputStream(in));
			}else{
				g.loadGrapXml(mxUtils.readInputStream(this.getClass().getResourceAsStream("models/edge_label.xml")));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		ids = new ArrayList<String>();
		edgeids = new ArrayList<String>();
//		gd.setConnectableEdges(false);
//		gd.setAllowDanglingEdges(false);
//		gd.setDisconnectOnMove(false);
//
//
//		Object v1 = gd.insertVertex(gd.getDefaultParent(), getId(), "Hello", 20, 20, 160, 48,"box");
//		String iid =getId();
//		Object v2 = gd.insertVertex(gd.getDefaultParent(), iid, "World!", 200, 150, 120, 48);
//		Object e1 = gd.insertEdge(gd.getDefaultParent(), getId(), "", v1, v2);
		//g.setModel(gd.getModel());
//
//
		Object v2=((mxGraphModel)gd.getModel()).getCell("3");
		Object e4=((mxGraphModel)gd.getModel()).getCell("4");

		g.addGraphListener(new mxIEventListener(){

			@Override
			public void invoke(Object sender, mxEventObject evt) {
				display.asyncExec(new Runnable() {

					@Override
					public void run() {
//						s.select(s.getItem(0));
					}
				});
				System.out.println("listener:"+evt.getName()+":"+evt.getProperties());
				if (evt.getName().equals("isCompleted")) {
					boolean isCompleted = (boolean) evt.getProperty("isCompleted");
					if (isCompleted) {
						System.out.println("初始化完成，开始载入数据...");
					}
				}
				if (evt.getName().equals(MxGraphEvent.MOUSE_DOWN)){
					double x = (double) evt.getProperty("x");
					double y = (double) evt.getProperty("y");
					int button = (int) evt.getProperty("button");
					if (button == 0){
						String id= getId();
						ids.add(id);
//						Element node = mxDomUtils.createDocument().createElement("UserObject");
//						node.setAttribute("label", "node!");
//						node.setAttribute("tooltip", "akkdkdkdkdk");
//						node.setAttribute("placeholders", "1");
						int styleNum = (int) (Math.random()*4);
						int statusNum = (int) (Math.random()*4);
						String style;
						String status;
						if (styleNum==1) {
							style = style6;
						}else{
							style = style5;
						}
						if (statusNum==1) {
							status = "error";
						}else{
							status = "unconn";
						}
						Object v = gd.insertVertex(parentG,id, "node!", x, y, 60, 60, style);
						g.setTooltip(id, "<h1>abcd</h1>"+ "<img src='rwt-resources/graph/images/"+status+".png"+"'/>");
						g.addCellOverlay(id, "rwt-resources/graph/images/"+status+".png", 16, 16, status);
						lastids = getId();
						edgeids.add(lastids);
						Object edge = gd.insertEdge(parentG, lastids, "", v2, v, style4);
						g.setTooltip(lastids, "<h1>efgh</h1>"+ "<img src='rwt-resources/graph/images/"+status+".png"+"'/>");
					}else{
//						gd.insertEdge(gd.getDefaultParent(), getId(), "", v2, v);
//						g.setCellStyle("5", style3);
//						g.translateCell("5", 5, 3);
//						String newStyle = mxStyleUtils.setStyle(style4, "rotation", "80");
//						g.setCellStyle("5", newStyle);
//						g.setCellChildOffset("4", 0, 258, 8);
//
//						g.updateEdgeLabelPosition("4",258,8,80);
//						g.selectCell(id);
//						g.selectCells(ids.toArray(new String[]{}) );
//						g.zoomOut();
						for(String id:ids){
							g.removeCellOverlays(id);
						}
					}
				}else if (evt.getName().equals(MxGraphEvent.MOUSE_HOVER)){
					double x = (double) evt.getProperty("x");
					double y = (double) evt.getProperty("y");
					//String id = (String) evt.getProperty("id");
					hoverText.setText("aaaaaa");
					hoverText.pack();
					hoverText.setLocation((int)x, (int)y);
					hoverText.setVisible(true);
				}else if (evt.getName().equals(MxGraphEvent.MOUSE_LEAVE)){
					hoverText.setVisible(false);
				}

			}});
		g.addListener(SWT.MouseWheel, new Listener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleEvent(Event event) {
				System.out.println(event);

			}
		});
		g.setArrowOffset(0.8);
		g.setTextAutoRotation(true);
		display = Display.getCurrent();
//
//		final ServerPushSession pushSession = new ServerPushSession();
//		pushSession.start();
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				//Client client = RWT.getClient();
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				while(true){
//					//UISession uiSession = RWT.getUISession( display );
//					display.asyncExec(new Runnable() {
//
//						@Override
//						public void run() {
//
//							long m = tick++ % 3;
//							System.out.println("timer..."+m);
//							if (m==0){
//								g.setCellStyle("5", style1);
//							}else if (m==1){
//								g.setCellStyle("5", style2);
//							}else if (m==2){
//								g.setCellStyle("5", style3);
//							}
//						}
//					});
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
	}

}
