/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bozels;

import bozels.elements.Block;
import bozels.elements.Bozel;
import bozels.elements.Target;
import bozels.factories.*;
import bozels.models.MainModel;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author Bart Middag
 */
public class ReadXML {
    private JFileChooser fc;
    private final MainModel model;
    private Map<String,Object> factoryMap;
    private FileNameExtensionFilter xmlfilter;
    
    /**
     * Read XML (with file chooser)
     */
    public ReadXML(MainModel model) {
        this.model = model;
        model.setPaused(true);
        fc = new JFileChooser(".");
        xmlfilter = new FileNameExtensionFilter(model.getLocaleBundle().getString("READER_XML_FILES"), "xml");
        fc.setFileFilter(xmlfilter);
        fc.setDialogTitle(model.getLocaleBundle().getString("READER_XML_TITLE"));
        int returnVal = fc.showOpenDialog(model.getWindow());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            open(file);
        }
    }
    
    /**
     * Read XML (without file chooser)
     */
    public ReadXML(MainModel model, String levelName) {
        this.model = model;
        model.setPaused(true);
        File file;
        try {
            file = new File(new URI(levelName));
            open(file);
        } catch (URISyntaxException ex) {
            JOptionPane.showMessageDialog(model.getWindow(),model.getLocaleBundle().getString("READER_XML_ERROR"),model.getLocaleBundle().getString("ERROR"),JOptionPane.WARNING_MESSAGE);
            model.setWinState(2);
        }
    }
    
    /**
     * Open file and read it
     */
    public final void open(File file) {
        factoryMap = new HashMap<String,Object>();
        factoryMap.put("red", new RedBozelFactory(model));
        factoryMap.put("blue", new BlueBozelFactory(model));
        BozelFactory whiteBozelFactory = new WhiteBozelFactory(model);
        factoryMap.put("white", whiteBozelFactory);
        factoryMap.put("black", whiteBozelFactory);
        factoryMap.put("yellow", new YellowBozelFactory(model));
        factoryMap.put("big", new BigTargetFactory(model));
        factoryMap.put("small", new SmallTargetFactory(model));
        factoryMap.put("solid", new SolidBlockFactory(model));
        factoryMap.put("stone", new ConcreteBlockFactory(model));
        factoryMap.put("metal", new MetalBlockFactory(model));
        factoryMap.put("wood", new WoodenBlockFactory(model));
        factoryMap.put("ice", new IceBlockFactory(model));
        try {
            Document document = new SAXBuilder().build(file);
            Element root = document.getRootElement();
            if (!root.getName().equals("level")) throw new IOException();
            model.setLevelName(document.getBaseURI());
            for(Object obj: root.getChildren()) {
                Element child = (Element)obj;
                String name = child.getName();
                if(name.equals("bozel")) {
                    createBozel(Float.parseFloat(child.getAttributeValue("x")),
                            Float.parseFloat(child.getAttributeValue("y")),
                            child.getAttributeValue("type"),
                            Integer.parseInt(child.getAttributeValue("id")));
                }
                else if(name.equals("target")) {
                    createTarget(Float.parseFloat(child.getAttributeValue("x")),
                            Float.parseFloat(child.getAttributeValue("y")),
                            child.getAttributeValue("type"));
                }
                else if(name.equals("block")||name.equals("ellipse")) {
                    createBlock(Float.parseFloat(child.getAttributeValue("x")),
                            Float.parseFloat(child.getAttributeValue("y")),
                            Float.parseFloat(child.getAttributeValue("width")),
                            Float.parseFloat(child.getAttributeValue("height")),
                            -Float.parseFloat(child.getAttributeValue("angle")),
                            name.equals("ellipse"),
                            child.getAttributeValue("material"));
                }
            }
            model.setWinState(0);
        } catch (JDOMException ex) {
            JOptionPane.showMessageDialog(model.getWindow(),model.getLocaleBundle().getString("READER_XML_ERROR_UNREADABLE"),model.getLocaleBundle().getString("ERROR"),JOptionPane.WARNING_MESSAGE);
            model.setWinState(2);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(model.getWindow(),model.getLocaleBundle().getString("READER_XML_ERROR_UNREADABLE"),model.getLocaleBundle().getString("ERROR"),JOptionPane.WARNING_MESSAGE);
            model.setWinState(2);
        }
    }
    
    public final Bozel createBozel(float x, float y, String type, int id) {
        if (factoryMap.get(type)!=null) {
            BozelFactory bozelFactory = (BozelFactory)factoryMap.get(type);
            return bozelFactory.create(x,y,id);
        }
        else return null;
    }
    
    public final Target createTarget(float x, float y, String type) {
        if (factoryMap.get(type)!=null) {
            TargetFactory targetFactory = (TargetFactory)factoryMap.get(type);
            return targetFactory.create(x,y);
        }
        else return null;
    }
    
    public final Block createBlock(float x, float y, float width, float height, float angle, boolean ellipse, String material) {
        if (factoryMap.get(material)!=null) {
            BlockFactory blockFactory = (BlockFactory)factoryMap.get(material);
            return blockFactory.create(x,y,width,height,angle,ellipse);
        }
        else return null;
    }
}
