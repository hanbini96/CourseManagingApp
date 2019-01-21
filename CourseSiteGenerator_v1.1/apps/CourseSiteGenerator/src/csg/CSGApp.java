/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg;

import djf.AppTemplate;
import djf.components.AppClipboardComponent;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.components.AppWorkspaceComponent;
import java.util.Locale;
import csg.data.CSGData;
import csg.files.CSGFiles;
import csg.clipboard.OfficeHoursClipboard;
import csg.workspace.CSGWorkspace;
import static javafx.application.Application.launch;

/**
 *
 * @author hanbin
 */
public class CSGApp extends AppTemplate{
    /**
     * This is where program execution begins. Since this is a JavaFX app it
     * will simply call launch, which gets JavaFX rolling, resulting in sending
     * the properly initialized Stage (i.e. window) to the start method inherited
     * from AppTemplate, defined in the Desktop Java Framework.
     * 
     * @param args Command-line arguments, there are no such settings used
     * by this application.
     */
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
    
    @Override
    public AppClipboardComponent buildClipboardComponent(AppTemplate app) {
        return new OfficeHoursClipboard(this);
    }

    @Override
    public AppDataComponent buildDataComponent(AppTemplate app) {
        return new CSGData(this);
    }

    @Override
    public AppFileComponent buildFileComponent() {
        return new CSGFiles(this);
    }

    @Override
    public AppWorkspaceComponent buildWorkspaceComponent(AppTemplate app) {
        return new CSGWorkspace(this);        
    }
}
