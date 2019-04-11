/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package magicUWE.transformation.requirements;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import magicUWE.shared.MagicDrawElementOperations;
import magicUWE.shared.MessageWriter;
import magicUWE.shared.UWEDiagramType;
import magicUWE.stereotypes.UWEStereotypeClassGeneral;
import magicUWE.stereotypes.UWEStereotypeProcessFlow;
import magicUWE.stereotypes.requirements.UWEStereotypeRequirementsActions;
import magicUWE.stereotypes.requirements.UWEStereotypeRequirementsUseCases;
import magicUWE.stereotypes.tags.UWETagNavigationNode;
import magicUWE.stereotypes.tags.UWETagSystemAction;
import magicUWE.stereotypes.tags.UWETagUserAction;
import magicUWE.stereotypes.tags.requirements.UWETagWebUseCase;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.PresentationElementsManager;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.uml.DiagramTypeConstants;
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement;
import com.nomagic.magicdraw.uml.symbols.PresentationElement;
import com.nomagic.magicdraw.uml.symbols.paths.PathElement;
import com.nomagic.magicdraw.uml.symbols.shapes.ShapeElement;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Action;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.CallBehaviorAction;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.InputPin;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OutputPin;
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.Pin;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityEdge;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityFinalNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.InitialNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ObjectFlow;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.ActivityNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.CentralBufferNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.DecisionNode;
import com.nomagic.uml2.ext.magicdraw.activities.mdstructuredactivities.StructuredActivityNode;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdmodels.Model;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralBoolean;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Slot;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.ext.magicdraw.mdusecases.UseCase;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.TypedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Operation;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.OpaqueExpression;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ValueSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Association;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import magicUWE.transformation.customdata.ConstraintCustom;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.AggregationKindEnum;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralUnlimitedNatural;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.MultiplicityElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralBoolean;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString;

/**
 *
 * @author PST LMU
 */
public class ReqToProTransformations {
	// Additional distance between the process classes in a diagram when
	// computing collisions
	public static final int iMIN_DISTANCE_X = 20;
	public static final int iMIN_DISTANCE_Y = 20;
	public static final String CONFIRMATION = "Confirmation";
	public static final String VALIDATE_OPERATION = "validateData";
	public static final String VALIDATE_DATA = "valid = true";
	
	private static ArrayList<NamedElement> listAssFrom= new ArrayList<NamedElement>();
	private static ArrayList<NamedElement> listAssTo = new ArrayList<NamedElement>();
	 
	

	/**
	 * Removes all workflows form a process class
	 * 
	 * @param cClass
	 *            process class
	 */
	public static void clearWorkflows(Class cClass) {
		ArrayList<NamedElement> listActivities = ElementCollector.getNamedElements(null, null, Activity.class, true,
				cClass, false);

		for (int i = listActivities.size() - 1; i >= 0; i--) {
			listActivities.get(i).dispose();
		}
	}
	
	
	/** Computes the candidates for process classes
     * 
     * @param bRemoveDuplicates remove candidates with same name?
     * @return list of candidates
     */
    public static ArrayList<NamedElement> getProcessClassCandidates(boolean bRemoveDuplicates)
    {
    	listAssFrom.clear();
    	listAssTo.clear();
        ArrayList<NamedElement> listReqModels=ElementCollector.getNamedElements(UWEDiagramType.USE_CASE.modelStereotype, null, Model.class, false, true);
        if ((listReqModels==null)||(listReqModels.isEmpty()))
        {
            return(new ArrayList<NamedElement>());
        }
        for(int i =0; i< listReqModels.size(); i++)
        {
        	Application.getInstance().getGUILog().log("listReqModels(): " + i + " "+ listReqModels.get(i).getName());
        }
        
        ArrayList<NamedElement> listReturn=ElementCollector.getNamedElements(UWEStereotypeRequirementsUseCases.PROCESSING_USECASE.toString(), null, UseCase.class, false, listReqModels, false);
        
        ArrayList<NamedElement> listBrowsing=ElementCollector.getNamedElements(UWEStereotypeRequirementsUseCases.BROWSING_USECASE.toString(), null, UseCase.class, false, listReqModels, false);
        for (int i=0 ;i < listBrowsing.size(); i++) {
        	
        	listReturn.add(listBrowsing.get(i));
        }
        
        
        

        if (bRemoveDuplicates)
        {
            for (int i=listReturn.size()-1;i>=0;i--)
            {
            	Application.getInstance().getGUILog().log("listReturn(): " + i + " "+ listReturn.get(i).getName());
                if (ElementCollector.getNamedElementFromArrayList(listReturn, listReturn.get(i).getName(), false,false)<i)
                {
                    listReturn.remove(i);
                }
            }
        }

        ArrayList<NamedElement> listPackages=ElementCollector.getNamedElements(UWEStereotypeRequirementsUseCases.PROCESSING_PACKAGE.toString(), null, Package.class, false, listReqModels, false);
        if (!listPackages.isEmpty())
        {
        	
        	
        	for(int i =0; i< listPackages.size(); i++)
            {
        		Application.getInstance().getGUILog().log("listPackages(): " + i + " "+ listPackages.get(i).getName());
            }
            ArrayList<NamedElement> listTemp=ElementCollector.getNamedElements(null, null, UseCase.class, false, listPackages, false);
            for (int i=0;i<listTemp.size();i++)
            {
            	//Application.getInstance().getGUILog().log("listTemp(): " + i + " "+ listTemp.get(i).getName());
                if (ElementCollector.getNamedElementFromArrayList(listReturn, listTemp.get(i).getName(), false,false)==ElementCollector.iNO_ELEMENT)
                {
                    listReturn.add(listTemp.get(i));
                   
                }
            }
        }
    	// get candidate from activity diagram
		// UserAction=> Process Class
		// System Action => Process Class
        // Model of UseCase -> ProcessClass
        ArrayList<NamedElement> listUseCaseModel=ElementCollector.getNamedElements(UWEStereotypeRequirementsUseCases.WEBUSECASE_USECASE.toString(), null, Model.class, false, listReqModels, false);
        for(int i =0; i< listUseCaseModel.size(); i++)
        {
        	//listReturn.add(listUseCaseModel.get(i));
    		Application.getInstance().getGUILog().log("listUseCaseModel(): " + i + " "+ listUseCaseModel.get(i).getName());
        }
        
        
       /* ArrayList<NamedElement> listUseCaseModel_=ElementCollector.getNamedElements(UWEStereotypeRequirementsUseCases.WEBUSECASE_PACKAGE.toString(), null, Model.class, false, listReqModels, false);
        for(int i =0; i< listUseCaseModel_.size(); i++)
        {
    		Application.getInstance().getGUILog().log("listUseCaseModel(): " + i + " "+ listUseCaseModel_.get(i).getName());
        }*/
        
        
        
        
        
		ArrayList<NamedElement> listActivities = ElementCollector.getNamedElements(null, null, Activity.class, true,
				listReqModels, false);
		for (int i = 0; i < listActivities.size(); i++) {
			Iterator<ActivityNode> itNodes = ((Activity) (listActivities.get(i))).getNode().iterator();
			ArrayList<ActivityNode> listOldNodes = new ArrayList<ActivityNode>();
			while (itNodes.hasNext()) {
				listOldNodes.add(itNodes.next());
			
			}
			for (int q = 0; q < listOldNodes.size(); q++) {
				ActivityNode cTemp = listOldNodes.get(q);
				if (cTemp instanceof CallBehaviorAction) {
					CallBehaviorAction cOld = ((CallBehaviorAction) (cTemp));
					if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.USER_ACTION.toString())) {
						//listReturn.add(cOld.getName());
						listReturn.add(cOld);
						
					}else if(StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.SYSTEM_ACTION.toString())) {
						Property property = StereotypesHelper.getPropertyByName(StereotypesHelper.getStereotype(Application.getInstance().getProject(),
										UWEStereotypeProcessFlow.SYSTEM_ACTION.toString()),
								UWETagSystemAction.CONFIRMED.toString());
						if(property != null) {
							
							
							
							String confirmedClassName = cOld.getName()+ "Confirmation";
						    CallBehaviorAction newObj =Application.getInstance().getProject().getElementsFactory().createCallBehaviorActionInstance();
							newObj.setName(confirmedClassName);
							listAssFrom.add(listActivities.get(i));
							listAssTo.add(newObj);
							listReturn.add(newObj);
							
							
	
						}
						
						
					}
					
				}
			
			}
		
		}
		
		//Remove duplicate

		if (bRemoveDuplicates) {
			for (int i = listReturn.size() - 1; i >= 0; i--) {
				if (ElementCollector.getNamedElementFromArrayList(listReturn, listReturn.get(i).getName(), false,
						false) < i) {
					listReturn.remove(i);
				}
			}
		}
     
        return(listReturn);
    }
    
    



	/**
	 * Creates a process class
	 * 
	 * @param sName
	 *            name of the class
	 * @param position
	 *            position of the class
	 * @param elParent
	 *            parent element
	 * @return class and presentation element
	 */
	public static ElementCollector.ReturnElement createProcessClass(String sName, Point position,
			NamedElement elParent) {
		boolean bCreated = false;
		if (!SessionManager.getInstance().isSessionCreated()) {
			bCreated = true;
			SessionManager.getInstance().createSession("Create Process Class");
		}
		Project cProject = Application.getInstance().getProject();
		DiagramPresentationElement cDiagram = cProject.getActiveDiagram();
		ArrayList<NamedElement> listOld = ElementCollector.getNamedElements(null, sName.replaceAll(" ", ""),
				Class.class, true, elParent, true);

		Class cReturn = null;
		ShapeElement elShape = null;
		Stereotype cStereotype = null;
		if (cDiagram != null) {
			if (listOld.isEmpty()) {
				cReturn = Application.getInstance().getProject().getElementsFactory().createClassInstance();

				if (elParent == null) {
					cReturn.setOwner(cDiagram.getDiagram().getOwner());
				} else {
					cReturn.setOwner(elParent);
				}
				cReturn.setName(sName.replaceAll(" ", ""));
				cStereotype = StereotypesHelper.getStereotype(Application.getInstance().getProject(),
						UWEStereotypeClassGeneral.PROCESS_CLASS.toString());
			} else {
				cReturn = ((Class) (listOld.get(0)));
			}

			try {
				elShape = PresentationElementsManager.getInstance().createShapeElement(cReturn, cDiagram);
				PresentationElementsManager.getInstance().reshapeShapeElement(elShape,
						new Rectangle(position.x, position.y, 0, 0));
			} catch (Exception e) {
				Application.getInstance().getGUILog()
						.showError("Exception in ReqToProTransformationRules.createProcessClass(): " + e.toString());
			}

			if (cStereotype != null) {
				StereotypesHelper.addStereotype(cReturn, cStereotype);

				ArrayList<NamedElement> listReqModels = ElementCollector
						.getNamedElements(UWEDiagramType.USE_CASE.modelStereotype, null, Model.class, true, true);
				ArrayList<NamedElement> listCases = ElementCollector.getNamedElements(
						UWEStereotypeRequirementsUseCases.PROCESSING_USECASE.toString(), sName, UseCase.class, true,
						listReqModels, false);
				if (listCases.size() > 0) {
					Property propOld = StereotypesHelper.getPropertyByName(
							StereotypesHelper.getStereotype(cProject,
									UWEStereotypeRequirementsUseCases.PROCESSING_USECASE.toString()),
							UWETagWebUseCase.IS_LANDMARK.toString());

					Slot slotOld = StereotypesHelper.getSlot(listCases.get(0), propOld, false, false);
					if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
						Property propNew = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(cProject,
										UWEStereotypeClassGeneral.PROCESS_CLASS.toString()),
								UWETagNavigationNode.IS_LANDMARK.toString());
						Slot slotNew = StereotypesHelper.getSlot(cReturn, propNew, true, false);
						LiteralBoolean litBoolean = cProject.getElementsFactory().createLiteralBooleanInstance();
						litBoolean.setValue(((LiteralBoolean) (slotOld.getValue().get(0))).isValue());
						slotNew.getValue().add(litBoolean);
					}

					propOld = StereotypesHelper.getPropertyByName(
							StereotypesHelper.getStereotype(cProject,
									UWEStereotypeRequirementsUseCases.PROCESSING_USECASE.toString()),
							UWETagWebUseCase.GUARD.toString());

					slotOld = StereotypesHelper.getSlot(listCases.get(0), propOld, false, false);
					if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
						Property propNew = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(cProject,
										UWEStereotypeClassGeneral.PROCESS_CLASS.toString()),
								UWETagNavigationNode.GUARD.toString());
						Slot slotNew = StereotypesHelper.getSlot(cReturn, propNew, true, false);
						LiteralString litString = cProject.getElementsFactory().createLiteralStringInstance();
						litString.setValue(((LiteralString) (slotOld.getValue().get(0))).getValue());
						slotNew.getValue().add(litString);
					}
				}

				ArrayList<NamedElement> listPackages = ElementCollector.getNamedElements(
						UWEStereotypeRequirementsUseCases.PROCESSING_PACKAGE.toString(), null, Package.class, true,
						listReqModels, false);
				for (int p = 0; p < listPackages.size(); p++) {
					listCases = ElementCollector.getNamedElements(null, sName, UseCase.class, true, listPackages.get(p),
							false);

					if (listCases.size() > 0) {
						Property propOld = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(cProject,
										UWEStereotypeRequirementsUseCases.PROCESSING_PACKAGE.toString()),
								UWETagWebUseCase.IS_LANDMARK.toString());

						Slot slotOld = StereotypesHelper.getSlot(listPackages.get(p), propOld, false, false);
						if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
							Property propNew = StereotypesHelper.getPropertyByName(
									StereotypesHelper.getStereotype(cProject,
											UWEStereotypeClassGeneral.PROCESS_CLASS.toString()),
									UWETagNavigationNode.IS_LANDMARK.toString());
							Slot slotNew = StereotypesHelper.getSlot(cReturn, propNew, true, false);
							LiteralBoolean litBoolean = cProject.getElementsFactory().createLiteralBooleanInstance();
							litBoolean.setValue(((LiteralBoolean) (slotOld.getValue().get(0))).isValue());
							slotNew.getValue().add(litBoolean);
						}

						propOld = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(cProject,
										UWEStereotypeRequirementsUseCases.PROCESSING_PACKAGE.toString()),
								UWETagWebUseCase.GUARD.toString());

						slotOld = StereotypesHelper.getSlot(listPackages.get(p), propOld, false, false);
						if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
							Property propNew = StereotypesHelper.getPropertyByName(
									StereotypesHelper.getStereotype(cProject,
											UWEStereotypeClassGeneral.PROCESS_CLASS.toString()),
									UWETagNavigationNode.GUARD.toString());
							Slot slotNew = StereotypesHelper.getSlot(cReturn, propNew, true, false);
							LiteralString litString = cProject.getElementsFactory().createLiteralStringInstance();
							litString.setValue(((LiteralString) (slotOld.getValue().get(0))).getValue());
							slotNew.getValue().add(litString);
						}
					}
				}

			}

			if ((bCreated) && (SessionManager.getInstance().isSessionCreated())) {
				SessionManager.getInstance().closeSession();
			}
			try {
				com.nomagic.magicdraw.properties.Property cProp = elShape.getProperty("SUPPRESS_CLASS_ATTRIBUTES")
						.clone();
				cProp.setValue(true);
				elShape.changeProperty(cProp);
				cProp = elShape.getProperty("SUPPRESS_CLASS_OPERATIONS").clone();
				cProp.setValue(true);
				elShape.changeProperty(cProp);
				cProp = elShape.getProperty("STEREOTYPES_DISPLAY_MODE").clone();
				cProp.setValue("STEREOTYPE_DISPLAY_MODE_ICON");
				elShape.changeProperty(cProp);

				if (!SessionManager.getInstance().isSessionCreated()) {
					bCreated = true;
					SessionManager.getInstance().createSession("Create Process Class");
				}
				PresentationElementsManager.getInstance().reshapeShapeElement(elShape,
						new Rectangle(position.x, position.y, 0, 0));
			} catch (Exception e) {
				Application.getInstance().getGUILog()
						.showError("Exception in ReqToProTransformationRules.createProcessClass(): " + e.toString());
			}
		}

		if ((bCreated) && (SessionManager.getInstance().isSessionCreated())) {
			SessionManager.getInstance().closeSession();
		}

		return (new ElementCollector.ReturnElement(cReturn, elShape, 0, null, null));
	}

	/**
	 * Add User Action and OCL
	 * 
	 * 
	 */
	/*public static void addOCLConstrains(Class cMain, NamedElement packProcess) {

		MessageWriter.log("[addOCLConstrains] cMain :   " + cMain.getName(), null);
		ArrayList<NamedElement> listReqModels = ElementCollector
				.getNamedElements(UWEDiagramType.USE_CASE.modelStereotype, null, Model.class, false, true);

		ArrayList<NamedElement> listReqCons = ElementCollector.getNamedElements(null, null, Constraint.class, true,
				listReqModels, false);
		ArrayList<NamedElement> listActivities = ElementCollector.getNamedElements(null, null, Activity.class, true,
				listReqModels, false);

		

		for (int i = 0; i < listActivities.size(); i++) {

			if ((listActivities.get(i).getName()).equals(cMain.getName()) == false) {
				MessageWriter.log("[addOCLConstrains] Skip Checking: cMain  " + cMain.getName() + " listActivities "
						+ listActivities.get(i).getName(), null);
				continue;
			}
			MessageWriter.log("[addOCLConstrains] Continue Checking: cMain  " + cMain.getName() + " listActivities "
					+ listActivities.get(i).getName(), null);

			Iterator<ActivityNode> itNodes = ((Activity) (listActivities.get(i))).getNode().iterator();
			ArrayList<ActivityNode> listOldNodes = new ArrayList<ActivityNode>();
			ArrayList<ActivityNode> listOldNodeParents = new ArrayList<ActivityNode>();
			while (itNodes.hasNext()) {
				listOldNodes.add(itNodes.next());
				listOldNodeParents.add(null);
			}
			Iterator<ActivityEdge> itEdges = ((Activity) (listActivities.get(i))).getEdge().iterator();
			ArrayList<ActivityEdge> listEdges = new ArrayList<ActivityEdge>();
			ArrayList<ActivityNode> listEdgeParents = new ArrayList<ActivityNode>();
			ArrayList<NamedElement> listSysAction = new ArrayList<NamedElement>();
			

			while (itEdges.hasNext()) {
				listEdges.add(itEdges.next());
				listEdgeParents.add(null);
			}

			for (int q = 0; q < listOldNodes.size(); q++) {
				ActivityNode cTemp = listOldNodes.get(q);
				if (cTemp instanceof CallBehaviorAction) {

					CallBehaviorAction cOld = ((CallBehaviorAction) (cTemp));
					if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.USER_ACTION.toString())) {
						MessageWriter.log("[addOCLConstrains] USER_ACTION :   " + cOld.getName(), null);
						ArrayList<TypedElement> listOutPins = new ArrayList<TypedElement>();
						ElementCollector.ReturnElement elementClass;

						int iI = 0;
						Point posTemp = new Point(50 + 50 * iI / 5, 50 + 50 * iI % 5);
						elementClass = createProcessClass(cOld.getName(), posTemp, packProcess);
						Iterator<InputPin> itInputPins = cOld.getInput().iterator();
						while (itInputPins.hasNext()) {
							InputPin inOldPin = itInputPins.next();
							// MessageWriter.log("[addOCLConstrains] IN PUT PIN
							// : " + inOldPin.getName(), null);

						}

						Iterator<OutputPin> itOutputPins = cOld.getOutput().iterator();
						while (itOutputPins.hasNext()) {
							OutputPin outOldPin = itOutputPins.next();
							// MessageWriter.log("[addOCLConstrains] OUT PUT PIN
							// : " + outOldPin.getName(), null);
							listOutPins.add((TypedElement) outOldPin);
							if (!outOldPin.get_constraintOfConstrainedElement().isEmpty()) {

								for (Object u : outOldPin.get_constraintOfConstrainedElement().toArray()) {
									// MessageWriter.log("CONSTRAIN: " +
									// ((Constraint) u).getName(), null);

								}

							}
						}

						ReqToConTransformations.createAttributes(listOutPins, elementClass.cClass);

						for (int k = 0; k < listOutPins.size(); k++) {

							if (!listOutPins.get(k).get_constraintOfConstrainedElement().isEmpty()) {
								ArrayList<Constraint> listEleConstraint = new ArrayList<Constraint>();
								for (Object u : listOutPins.get(k).get_constraintOfConstrainedElement().toArray()) {
									for (int j = 0; j < listReqCons.size(); j++) {
										if (listReqCons.get(j) == u) {
											listEleConstraint.add((Constraint) listReqCons.get(j));
											
										}
									}
								}

								ReqToConTransformations.createConstraint(listEleConstraint,
										listOutPins.get(k).getName(),
										listOutPins.get(k).getOwner().getOwner().getHumanName(), elementClass.cClass);
							}

						}

					}

					// system action
					if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.SYSTEM_ACTION.toString())) {
						//check confirmed tag
						Property property = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(Application.getInstance().getProject(),
										UWEStereotypeProcessFlow.SYSTEM_ACTION.toString()),
								UWETagSystemAction.CONFIRMED.toString());
						if(property != null) {
							MessageWriter.log("[addOCLConstrains] @@@@@@@@@@@@@SYSTEM_ACTION:   " + cOld.getName(), null);
							MessageWriter.log("[addOCLConstrains] @@@@@@@@@@@  SYSTEM_ACTION: CONFIRMED    " + property.getName(), null);
							//create Confirmed process class 
							String confirmedClassName = cOld.getName()+ "Confirmation";
							ElementCollector.ReturnElement elementClass;

							int iI = 0;
							Point posTemp = new Point(50 + 50 * iI / 5, 50 + 50 * iI % 5);
							elementClass = createProcessClass(confirmedClassName, posTemp, packProcess);
							
							
						}
						
						

						
						
						
						
						
						
						MessageWriter.log("[addOCLConstrains] SYSTEM_ACTION:   " + cOld.getName(), null);
						ArrayList<Constraint> listEleConstraint = new ArrayList<Constraint>();
						ArrayList<NamedElement> listSysAction1 = new ArrayList<NamedElement>();
						ArrayList<ActivityEdge> listObjectFlow = new ArrayList<ActivityEdge>();
						listSysAction.add(cOld);
						listSysAction1.add(cOld);
						// ReqToConTransformations.createOperation(listSysAction,cMain);

						for (int s = 0; s < listEdges.size(); s++) {
							if (listEdges.get(s) instanceof ObjectFlow) {
								ObjectFlow flowOld = ((ObjectFlow) (listEdges.get(s)));
								MessageWriter.log("[addOCLConstrains]   listEdges: Source    " + flowOld.getSource().getName()+ "  target: " + flowOld.getTarget().getName(), null);
								if (flowOld.getTarget().getName().equals(cOld.getName())) {
									 listObjectFlow.add(flowOld);

									if (!flowOld.get_constraintOfConstrainedElement().isEmpty()) {

										for (Object u : flowOld.get_constraintOfConstrainedElement().toArray()) {
											MessageWriter.log("[addOCLConstrains] FLOW CONSTRAIN:   " + ((Constraint) u).getName(), null);
											listEleConstraint.add((Constraint) u);

										}

									}

								}

							}
						}
						createOperation(cOld,listObjectFlow, listEleConstraint,cMain );

					}
					/////END SYSACTION CHECK

				}//END ALL NODE CHECK

				for (int l = 0; l < listSysAction.size(); l++) {
					MessageWriter.log("[addOCLConstrains]  cMain " + cMain.getName() + "  listSysAction:    "
							+ listSysAction.get(l).getName(), null);

				}
				// ReqToConTransformations.createOperation(listSysAction,cMain);
			}

		} // end all activity

	}*/
	
	private static Class getProClass(ArrayList<ElementCollector.ReturnElement> listProElements, String className, boolean isConfirm) {
		/*if(isValidate) {
			className += "";
		}else */if(isConfirm) {
			className += CONFIRMATION;
			
		}
		for(int i =0; i< listProElements.size(); i++ ) {
			if(listProElements.get(i).cClass.getName().equals(className))
				return listProElements.get(i).cClass;
		}
		return null;
	}
	
	
	public static void createAtributeOperationAndConstrains( ArrayList<ElementCollector.ReturnElement> listProElements, NamedElement packProcess) {
		HashMap<String,ArrayList<String>> hashMapAtribute=new HashMap<String,ArrayList<String>>(); 
		HashMap<String,ArrayList<String>> hashMapOpe=new HashMap<String,ArrayList<String>>(); 
		
		for(int t =0; t < listProElements.size(); t ++) {
			ArrayList<String> arr= new ArrayList<String>();
			hashMapAtribute.put(listProElements.get(t).cClass.getName(),arr );
			hashMapOpe.put(listProElements.get(t).cClass.getName(),arr );
			
			
		}
		
		MessageWriter.log("[addOCLConstrains] (+)", null);
		
		ArrayList<NamedElement> listReqModels = ElementCollector
				.getNamedElements(UWEDiagramType.USE_CASE.modelStereotype, null, Model.class, false, true);

		ArrayList<NamedElement> listReqCons = ElementCollector.getNamedElements(null, null, Constraint.class, true,
				listReqModels, false);
		ArrayList<NamedElement> listActivities = ElementCollector.getNamedElements(null, null, Activity.class, true,
				listReqModels, false);

		

		for (int i = 0; i < listActivities.size(); i++) {
			//MessageWriter.log("[addOCLConstrains] Continue Checking: cMain  " + cMain.getName() + " listActivities "
				//	+ listActivities.get(i).getName(), null);

			Iterator<ActivityNode> itNodes = ((Activity) (listActivities.get(i))).getNode().iterator();
			ArrayList<ActivityNode> listOldNodes = new ArrayList<ActivityNode>();
			ArrayList<ActivityNode> listOldNodeParents = new ArrayList<ActivityNode>();
			while (itNodes.hasNext()) {
				listOldNodes.add(itNodes.next());
				listOldNodeParents.add(null);
			}
			Iterator<ActivityEdge> itEdges = ((Activity) (listActivities.get(i))).getEdge().iterator();
			ArrayList<ActivityEdge> listEdges = new ArrayList<ActivityEdge>();
			ArrayList<ActivityNode> listEdgeParents = new ArrayList<ActivityNode>();
			ArrayList<NamedElement> listSysAction = new ArrayList<NamedElement>();
			
			ArrayList<NamedElement> listParamOfOperation = new ArrayList<NamedElement>();
			Class proClass = null;
			

			while (itEdges.hasNext()) {
				listEdges.add(itEdges.next());
				listEdgeParents.add(null);
			}

			for (int q = 0; q < listOldNodes.size(); q++) {
				ActivityNode cTemp = listOldNodes.get(q);
				if (cTemp instanceof CallBehaviorAction) {
					listParamOfOperation.clear();

					CallBehaviorAction cOld = ((CallBehaviorAction) (cTemp));
					if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.USER_ACTION.toString())) {// INPUT USER ACTION
						//MessageWriter.log("[addOCLConstrains] USER_ACTION :   " + cOld.getName(), null);
						ArrayList<TypedElement> listOutPins = new ArrayList<TypedElement>();
						ElementCollector.ReturnElement elementClass;
						proClass= getProClass(listProElements,cOld.getName(), false );
						if(proClass == null)
							continue;
						
						// check exist in hash map
						ArrayList<String> listAtr = null;
						listAtr = hashMapAtribute.get(proClass.getName());
						ArrayList<String> listOpe = null;
						listOpe = hashMapOpe.get(proClass.getName());
						
						ArrayList<TypedElement> listAtributes = new ArrayList<TypedElement>();
						ArrayList<NamedElement> listParams = new ArrayList<NamedElement>();
						ArrayList<NamedElement> listInvConstrains = new ArrayList<NamedElement>();
						ArrayList<NamedElement> listPrePostContrains = new ArrayList<NamedElement>();
						ArrayList<Constraint> listConstrains = new ArrayList<Constraint>();
						
						
						Iterator<OutputPin> itOutputPins = cOld.getOutput().iterator();
						while (itOutputPins.hasNext()) {
							OutputPin outOldPin = itOutputPins.next();
							boolean isAdded = false;
							
							if(listAtributes != null) {
								for(int x = 0; x< listAtributes.size(); x++) {
									if(listAtributes.get(x).equals(outOldPin)) {
										isAdded = true;
										break;
									}
								}
							}
							if(!isAdded && listAtributes != null) {
								//listOutPins.add((TypedElement) outOldPin);
								//listAtr.add(outOldPin.getName());
								listAtributes.add(outOldPin);
								
								Application.getInstance().getGUILog().log("###listAtributes##### + " + outOldPin.getName());
								ArrayList<Constraint> attributeConstrains = new ArrayList<Constraint>();
								
								if (!outOldPin.get_constraintOfConstrainedElement().isEmpty()) {
									//Application.getInstance().getGUILog().log("addOCLConstrains get_constraintOfConstrainedElement111 ");

									for (Object u : outOldPin.get_constraintOfConstrainedElement().toArray()) {
										//Application.getInstance().getGUILog().log("addOCLConstrains get_constraintOfConstrainedElement 2222 ");
										
										listConstrains.add((Constraint) u);
										listParams.add(outOldPin);
										Application.getInstance().getGUILog().log("listParams##### + " + outOldPin.getName());
										attributeConstrains.add((Constraint) u);
										

									}

								}
								
								createAttributes(outOldPin,attributeConstrains, proClass);// CREATE ATRIBUTE and INV OCL
								
									
								
							}else {
								//Application.getInstance().getGUILog().log("addOCLConstrains_():  Atribute is ADDED class " + proClass.getName()  + "Atr name: " +  outOldPin.getName());
								
							}

							
						}
						
					
						ArrayList<Constraint> listEleConstraint = new ArrayList<Constraint>();
						
						ArrayList<ConstraintCustom> listEleConstraintCustom = new ArrayList<ConstraintCustom>();

						
						//create validate data operation with OCL input data
						Property property = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(Application.getInstance().getProject(),
										UWEStereotypeProcessFlow.USER_ACTION.toString()),
								UWETagUserAction.VALIDATED.toString());
						
					  if(property != null) {
						  Application.getInstance().getGUILog().log("VALIDATE_OPERATION starting  ");
							boolean isAdded = false;
							String opeName = VALIDATE_OPERATION;
							
							CallBehaviorAction newOpe =Application.getInstance().getProject().getElementsFactory().createCallBehaviorActionInstance();
							newOpe.setName(opeName);
							createOperation(newOpe,listParams, listConstrains, proClass );
							
								
							
						}

					}

					// system action
					if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.SYSTEM_ACTION.toString())) {
						listParamOfOperation.clear();
						
						
					    Iterator<InputPin> itInputPins = cOld.getInput().iterator();
						while (itInputPins.hasNext()) {
							InputPin inOldPin = itInputPins.next();
							boolean isAdded = false;
							
							if(listParamOfOperation != null) {
								for(int x = 0; x< listParamOfOperation.size(); x++) {
									if(listParamOfOperation.get(x).equals(inOldPin)) {
										isAdded = true;
										break;
									}
								}
							}
							if(!isAdded && listParamOfOperation != null) {
								
								listParamOfOperation.add(inOldPin);
									
								
							}else {
								//Application.getInstance().getGUILog().log("addOCLConstrains_():  Atribute is ADDED class " + proClass.getName()  + "Atr name: " +  outOldPin.getName());
								
							}
								

						}
						
						
						Property property = StereotypesHelper.getPropertyByName(StereotypesHelper.getStereotype(Application.getInstance().getProject(),
								UWEStereotypeProcessFlow.SYSTEM_ACTION.toString()),
						UWETagSystemAction.CONFIRMED.toString());
						
						if(property != null) {
							
							proClass= getProClass(listProElements,cOld.getName(), true );
							
						}else {
							proClass= getProClass(listProElements,cOld.getName(), false );
						}
						if(proClass == null) {
							continue;
						}
						
						createOperation(cOld,listParamOfOperation, null,proClass );
						

					}
					/////END SYSACTION CHECK

				}//END ALL NODE CHECK

			}

		} // end all activity

	}
	
	
	public static void createAttributes(TypedElement attributeElement, ArrayList<Constraint> listConstraint, Class cMain) {
		if(attributeElement == null){
			return;
		}
			Property propTemp = Application.getInstance().getProject().getElementsFactory().createPropertyInstance();

			if (Property.class.isInstance(attributeElement)) {
				if (((Property) (attributeElement)).getDefaultValue() != null) {
					if (((Property) (attributeElement)).getDefaultValue() instanceof LiteralInteger) {
						LiteralInteger litInt = Application.getInstance().getProject().getElementsFactory()
								.createLiteralIntegerInstance();
						litInt.setValue(
								((LiteralInteger) ((Property) (attributeElement)).getDefaultValue()).getValue());
						propTemp.setDefaultValue(litInt);
					} else if (((Property) (attributeElement))
							.getDefaultValue() instanceof LiteralUnlimitedNatural) {
						LiteralUnlimitedNatural litUN = Application.getInstance().getProject().getElementsFactory()
								.createLiteralUnlimitedNaturalInstance();
						litUN.setValue(((LiteralUnlimitedNatural) ((Property) (attributeElement)).getDefaultValue())
								.getValue());
						propTemp.setDefaultValue(litUN);
					} else if (((Property) (attributeElement)).getDefaultValue() instanceof LiteralString) {
						LiteralString litString = Application.getInstance().getProject().getElementsFactory()
								.createLiteralStringInstance();
						litString.setValue(
								((LiteralString) ((Property) (attributeElement)).getDefaultValue()).getValue());
						propTemp.setDefaultValue(litString);
					} else if (((Property) (attributeElement)).getDefaultValue() instanceof LiteralBoolean) {
						LiteralBoolean litBool = Application.getInstance().getProject().getElementsFactory()
								.createLiteralBooleanInstance();
						litBool.setValue(
								((LiteralBoolean) ((Property) (attributeElement)).getDefaultValue()).isValue());
						propTemp.setDefaultValue(litBool);
					}
				}
			}

			propTemp.setName(attributeElement.getName());
			propTemp.setType(attributeElement.getType());
			if (((MultiplicityElement) (attributeElement)).getUpperValue() != null) {
				if (((MultiplicityElement) (attributeElement)).getUpperValue() instanceof LiteralInteger) {
					LiteralInteger litInt = Application.getInstance().getProject().getElementsFactory()
							.createLiteralIntegerInstance();
					litInt.setValue(((LiteralInteger) ((MultiplicityElement) (attributeElement)).getUpperValue())
							.getValue());
					propTemp.setUpperValue(litInt);
				} else if (((MultiplicityElement) (attributeElement))
						.getUpperValue() instanceof LiteralUnlimitedNatural) {
					LiteralUnlimitedNatural litUN = Application.getInstance().getProject().getElementsFactory()
							.createLiteralUnlimitedNaturalInstance();
					litUN.setValue(
							((LiteralUnlimitedNatural) ((MultiplicityElement) (attributeElement)).getUpperValue())
									.getValue());
					propTemp.setUpperValue(litUN);
				}
			}
			if (((MultiplicityElement) (attributeElement)).getLowerValue() != null) {
				if (((MultiplicityElement) (attributeElement)).getLowerValue() instanceof LiteralInteger) {
					LiteralInteger litInt = Application.getInstance().getProject().getElementsFactory()
							.createLiteralIntegerInstance();
					litInt.setValue(((LiteralInteger) ((MultiplicityElement) (attributeElement)).getLowerValue())
							.getValue());
					propTemp.setLowerValue(litInt);
				} else if (((MultiplicityElement) (attributeElement))
						.getLowerValue() instanceof LiteralUnlimitedNatural) {
					LiteralUnlimitedNatural litUN = Application.getInstance().getProject().getElementsFactory()
							.createLiteralUnlimitedNaturalInstance();
					litUN.setValue(
							((LiteralUnlimitedNatural) ((MultiplicityElement) (attributeElement)).getLowerValue())
									.getValue());
					propTemp.setLowerValue(litUN);
				}
			}
			propTemp.setOwner(cMain);
			createInvConstraint(propTemp, listConstraint,cMain );
		
	}
	
	
	public static void createInvConstraint(Property propTemp, ArrayList<Constraint> listConstraint, Class cMain) {
		for (int i = 0; i < listConstraint.size(); i++) {
			Constraint consTemp = Application.getInstance().getProject().getElementsFactory()
					.createConstraintInstance();
			OpaqueExpression oETemp = Application.getInstance().getProject().getElementsFactory()
					.createOpaqueExpressionInstance();

			OpaqueExpression specTemp = (OpaqueExpression) (listConstraint.get(i).getSpecification());
			for (int j = 0; j < specTemp.getBody().size(); j++) {
				oETemp.getBody().add( specTemp.getBody().get(j));
			}

			oETemp.getLanguage().add("OCL2.0");
			
			Application.getInstance().getGUILog().log("createInvConstraint propTemp " +propTemp.getName() );
		

			consTemp.setName(listConstraint.get(i).getName());
			consTemp.setSpecification((ValueSpecification) (oETemp));
			
			consTemp.getConstrainedElement().add(propTemp);
			/*if (cMain.getOwnedAttribute().size() > 0) {
				for (int j = 0; j < cMain.getOwnedAttribute().size(); j++) {
					if (cMain.getOwnedAttribute().get(j).getName() == aName) {
						consTemp.getConstrainedElement().add(cMain.getOwnedAttribute().get(j));
					}
				}
			}*/
			consTemp.setOwner(cMain);

		}
	}
	
	
	  public static void createOperation( NamedElement eleOperation, ArrayList<NamedElement> listParamater, ArrayList<Constraint> listConstraint,
	            Class cMain)
	    {
		  if(cMain == null) {
			  return;
		  }
		  
		      MessageWriter.log("[createOperation] operation name :   " + eleOperation.getName() + " class name " + cMain.getName(), null);
	       
	        	Operation operTemp = Application.getInstance().getProject().getElementsFactory().createOperationInstance();
	        	operTemp.setName(eleOperation.getName());
	        	operTemp.setOwner(cMain);
	        	for(int i=0; i< listParamater.size(); i++){
		        	Parameter paramTemp =  Application.getInstance().getProject().getElementsFactory().createParameterInstance();
		        	//paramTemp.setName(listParamater.get(i).getSource().getName());
		        	paramTemp.setName(listParamater.get(i).getName());
		        	paramTemp.setOwner(operTemp);
		        	MessageWriter.log("[Parameter]#### Parameter for Opretion: " + eleOperation.getName() + "  at idx  :   " + i  + "  name : " +  listParamater.get(i).getName(), null);
	        	}
	        	
	        	if(listConstraint != null){
	        		boolean hasConstrain = false;
		        	for (int i=0;i<listConstraint.size();i++)
		            {
		        		hasConstrain = true;
		        		NamedElement m = null;
		        		String s = "";
		        		for (Object u : listConstraint.get(i).getConstrainedElement().toArray()) {
							// MessageWriter.log("CONSTRAIN: " +
							// ((Constraint) u).getName(), null);
		              		//Application.getInstance().getGUILog().log("MagicUWE: Requirements -> Presentation addPresentationChildren get_constraintOfConstrainedElement " + ((Constraint) u).getName())
		        			if(u instanceof NamedElement) {
			              		 m = (NamedElement)u;
			              		 if(m != null) {
			              			 s= m.getName();
			              			 break;
			              		 }
		              		 }
		              		
		              		Application.getInstance().getGUILog().log("MagicUWE: Requirements ************ -> createOperation " + m.getName());
	
						}
		        		
		        	
		        		
		        		MessageWriter.log("[createOperation]### listConstraint at idx  :   " + i  + "  name : " +  listConstraint.get(i).getName(), null);
		                Constraint consTemp=Application.getInstance().getProject().getElementsFactory().createConstraintInstance();
		                OpaqueExpression oETemp=Application.getInstance().getProject().getElementsFactory().createOpaqueExpressionInstance();
		                
		                OpaqueExpression specTemp = (OpaqueExpression)(listConstraint.get(i).getSpecification());
		                 for(int j=0;j<specTemp.getBody().size();j++)
		                {
		                	 MessageWriter.log("[createOperation] listConstraint:   " + specTemp.getBody().get(j), null);
		                	 oETemp.getBody().add("pre :self. "+ s + "."+ specTemp.getBody().get(j));
		                	// oETemp.getBody().add("self." + listConstraint.get(i). + "." + specTemp.getBody().get(j) + "()");
		                	 
		                }
		                 
		              
		                
		                oETemp.getLanguage().add("OCL2.0");
	
		                consTemp.setName(listConstraint.get(i).getName());
		                consTemp.setSpecification((ValueSpecification)(oETemp));
		                
		                consTemp.setContext(operTemp);
						consTemp.setPreContext(operTemp);
						//consTemp
						consTemp.setOwner(operTemp);
						
						
						
						
						
		               /* if(cMain.getOwnedAttribute().size() > 0)
		                {
		                	for(int j=0;j<cMain.getOwnedAttribute().size();j++)
		                	{
		                		if(cMain.getOwnedAttribute().get(j).getName() == aName)
		                		{
		                			consTemp.getConstrainedElement().add(cMain.getOwnedAttribute().get(j));
		                		}
		                	}
		                }*/
		               /* //tem block
		               consTemp.getConstrainedElement().add(operTemp);
		               //consTemp.getConstrainedElement().add(listParamater.get(0));// add constrain to method
		                for(int j=0;j<operTemp.getOwnedParameter().size();j++)
		                {
		                	consTemp.getConstrainedElement().add(operTemp.getOwnedParameter().get(j));//add constraint to params of method
		                }
		                consTemp.setOwner(cMain); 
		                //end*/
						
						
		                
		                
		               
		           }
		        	//add post 
		        	if(hasConstrain){
			        	Constraint consTemp=Application.getInstance().getProject().getElementsFactory().createConstraintInstance();
		                OpaqueExpression oETemp=Application.getInstance().getProject().getElementsFactory().createOpaqueExpressionInstance();
		                Application.getInstance().getGUILog().log("[createOperation] ADD POST:");
		                oETemp.getBody().add(VALIDATE_DATA);
		                oETemp.getLanguage().add("OCL2.0");
		                consTemp.setName("resultOk:");
		                consTemp.setSpecification((ValueSpecification)(oETemp));
		                
		                consTemp.setContext(operTemp);
						consTemp.setPostContext(operTemp);
						//consTemp
						consTemp.setOwner(operTemp);
			        }
		        	
		        	
		        	
	        	}
	        	
	        
	    }
	  
	  
	  
	
	  
	  
	  
	  public static void createOperation__( NamedElement eleOperation, ArrayList<NamedElement> listParamater, ArrayList<ConstraintCustom> listConstraint,
	            Class cMain)
	    {
		  if(cMain == null) {
			  return;
		  }
		  
		      MessageWriter.log("[createOperation] operation name :   " + eleOperation.getName() + " class name " + cMain.getName(), null);
	       
	        	Operation operTemp = Application.getInstance().getProject().getElementsFactory().createOperationInstance();
	        	operTemp.setName(eleOperation.getName());
	        	operTemp.setOwner(cMain);
	        	for(int i=0; i< listParamater.size(); i++){
		        	Parameter paramTemp =  Application.getInstance().getProject().getElementsFactory().createParameterInstance();
		        	//paramTemp.setName(listParamater.get(i).getSource().getName());
		        	paramTemp.setName(listParamater.get(i).getName());
		        	paramTemp.setOwner(operTemp);
		        	MessageWriter.log("[createOperation]#### Parameter at idx  :   " + i  + "  name : " +  listParamater.get(i).getName(), null);
	        	}
	        	
	        	
	        	for (int i=0;i<listConstraint.size();i++)
	            {
	        		
	        		
	        		MessageWriter.log("[createOperation]### listConstraint at idx  :   " + i  + "  name : " +  listConstraint.get(i).constraint.getName(), null);
	                Constraint consTemp=Application.getInstance().getProject().getElementsFactory().createConstraintInstance();
	                OpaqueExpression oETemp=Application.getInstance().getProject().getElementsFactory().createOpaqueExpressionInstance();
	                
	                OpaqueExpression specTemp = (OpaqueExpression)(listConstraint.get(i).constraint.getSpecification());
	                 for(int j=0;j<specTemp.getBody().size();j++)
	                {
	                	 MessageWriter.log("[createOperation] listConstraint:   " + specTemp.getBody().get(j), null);
	                	 oETemp.getBody().add("pre :self. " +listConstraint.get(i).owner.getName() + "." +  specTemp.getBody().get(j));
	                	// oETemp.getBody().add("self." + listConstraint.get(i). + "." + specTemp.getBody().get(j) + "()");
	                	 
	                }
	                 
	                 
	                 
	              
	                
	                oETemp.getLanguage().add("OCL2.0");
	                

	                consTemp.setName(listConstraint.get(i).constraint.getName());
	                consTemp.setSpecification((ValueSpecification)(oETemp));
	                
	                consTemp.setContext(operTemp);
					consTemp.setPreContext(operTemp);
					consTemp.setPostContext(operTemp);
					consTemp.setOwner(operTemp);
					
	               /* if(cMain.getOwnedAttribute().size() > 0)
	                {
	                	for(int j=0;j<cMain.getOwnedAttribute().size();j++)
	                	{
	                		if(cMain.getOwnedAttribute().get(j).getName() == aName)
	                		{
	                			consTemp.getConstrainedElement().add(cMain.getOwnedAttribute().get(j));
	                		}
	                	}
	                }*/
	                //tem block
	             /*  consTemp.getConstrainedElement().add(operTemp);
	               //consTemp.getConstrainedElement().add(listParamater.get(0));// add constrain to method
	                for(int j=0;j<operTemp.getOwnedParameter().size();j++)
	                {
	                	consTemp.getConstrainedElement().add(operTemp.getOwnedParameter().get(j));//add constraint to params of method
	                }
	                consTemp.setOwner(cMain); */
	                //end
					
					
	                
	                
	               
	           }
	        	
	        
	    }
	  
	  /*
	   * Add association
	   */
	  public static boolean addAssociation(ArrayList<ElementCollector.ReturnElement> listProClass, HashMap<Element, List<PresentationElement>> lisPre) {
		  Application.getInstance().getGUILog().log(" Req-> Process ****** addAssociation");
		  boolean bCreated=false;
		  if (!SessionManager.getInstance().isSessionCreated())
	        {
	            bCreated=true;
	            SessionManager.getInstance().createSession("Add Menu to Navigation Class");
	        }
		  Project project = Application.getInstance().getProject();
		  DiagramPresentationElement sourceDiagram = project.getActiveDiagram();
		  UWEDiagramType sourceDiagramType = UWEDiagramType.getDiagramType(sourceDiagram.getDiagram());
		  HashMap<Element, List<PresentationElement>> elementsInDiagram =
					new HashMap<Element, List<PresentationElement>>();
			MagicDrawElementOperations.collectElementsAndPresentationElements(sourceDiagram, elementsInDiagram);

		  for (Element element : elementsInDiagram.keySet()) {
			 // Application.getInstance().getGUILog().log(" Req-> Process ****** Element " + ((NamedElement)element).getName());
				List<PresentationElement> peList = elementsInDiagram.get(element);
				Class transformedClass = null;
				int j = 0;
				for (int i = 0; i < peList.size(); i++) {

					PresentationElement pe = peList.get(i);
					// Application.getInstance().getGUILog().log(" Req-> Process ****** addAssociation " + peList.get(i).getName());
				}
		  }
		 // return false;
		  
		  for(int i =0; i < listProClass.size(); i++) {
			 // Application.getInstance().getGUILog().log(" Req-> Process listProClass****** " +  listProClass.get(i).cClass.getName());
			  for( int k =0; k < listAssFrom.size(); k ++) {
				 // Application.getInstance().getGUILog().log(" Req-> Process listAssFrom****** " +  listAssFrom.get(k).getName());
				  if(listProClass.get(i).cClass.getName().equals(listAssFrom.get(k).getName())){
					  
					 Class cFrom = listProClass.get(i).cClass;
					  Class cTo = getProClass(listProClass,listAssTo.get(k).getName(), false );
					  Association cAssociation=null;
					  cAssociation=MagicDrawElementOperations.createAssociation(null);
					  //cAssociation = Application.getInstance().getProject().getElementsFactory().createAssociationInstance();

					 // Application.getInstance().getGUILog().log(" Req-> Process createAssociation****** From  " +  cFrom.getName() + "   to " + cTo.getName()  );

			            cAssociation.setOwner(cFrom.getOwner());
			            Application.getInstance().getProject().addElementByID(cAssociation,cAssociation.getID());

			            cAssociation.getOwnedEnd().get(1).setType(cTo);
			            cAssociation.getOwnedEnd().get(0).setType(cFrom);
			            cAssociation.getOwnedEnd().get(0).setOwner(cTo);
			            
			            
			            try {
			            	 Application.getInstance().getGUILog().log(" Req-> Process listAssFrom $$$$$$$ Before from " +  elementsInDiagram.get(cFrom).get(0).getName() + "  to " +  elementsInDiagram.get(cTo).get(0).getName() );
			            	PresentationElementsManager.getInstance().createPathElement(cAssociation,
			            			elementsInDiagram.get(cTo).get(0), elementsInDiagram.get(cFrom).get(0));
			            	List<Property> assocProperties = cAssociation.getMemberEnd();
			            	Application.getInstance().getGUILog().log("assocProperties size: " + assocProperties.size());
			    			if (assocProperties.size() > 0) {
			    				for (Property property : assocProperties) {
			    					ModelHelper.setNavigable(property, true);
			    				}
			    			}
			    			if (assocProperties.size() == 2) {
			    				// set composition
			    				if (/*compositionAtTheHeadClassEnd*/true) {
			    					assocProperties.get(1).setAggregation(AggregationKindEnum.COMPOSITE);
			    				}
			    			}
			            	
			            }catch (Exception e) {
			            	Application.getInstance().getGUILog().log(" Req-> Process EXPPPPPP" + e.getMessage());
			            	
			            }

					  
					  
				  }
			  }
		  }
		  
		  if ((bCreated) && (SessionManager.getInstance().isSessionCreated())) {
				SessionManager.getInstance().closeSession();
			}
		  return true;
		  
	  }
	    

	/**
	 * Adds a workflow to a process class
	 *
	 * @param cMain
	 *            the process class
	 */
	  
	  
	 public static boolean _addWorkflow() { 
		 Application.getInstance().getGUILog().log(" Req-> AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA Process addWorkflow (+)");
		 ArrayList<NamedElement> listReqModels = ElementCollector
					.getNamedElements(UWEDiagramType.USE_CASE.modelStereotype, null, Model.class, false, true);
		 ArrayList<NamedElement> listActivities = ElementCollector.getNamedElements(null, null, Activity.class, true,
					listReqModels, false);
		 for (int i = 0; i < listActivities.size(); i++) {
			 boolean bCreated = false;
				if (!SessionManager.getInstance().isSessionCreated()) {
					bCreated = true;
					SessionManager.getInstance().createSession("Create Workflow");
				}
				
				ArrayList<ShapeElement> listShapes = new ArrayList<ShapeElement>();
				
				int idx =0;
				
				ArrayList<Rectangle> listBounds = new ArrayList<Rectangle>();
				//ArrayList<ActivityNode>  _acNodes = new
				HashMap<ActivityNode, ActivityNode> acNodes = new HashMap<ActivityNode, ActivityNode>();
				HashMap<ActivityEdge, ActivityEdge> acEdges = new HashMap<ActivityEdge, ActivityEdge>();
				
				Activity cActivity = Application.getInstance().getProject().getElementsFactory().createActivityInstance();
				UWEDiagramType dgType = UWEDiagramType.PROCESS_FLOW;
				
				
				 
				
					Package pack =
							dgType.getModelOrCreateIt(Application.getInstance().getProjectsManager()// create process package
									.getActiveProject(),
									"Do you want the new diagram(s) to be stored in a new model called \""
											+ dgType.originalModelName + "\"\nincluding the stereotype \""
											+ dgType.modelStereotype
											+ "\"? (recommended) \n\n(\"No\" will ask you to select a package)",
									true);
			
				//dgType.createAndAddDiagram(pack, cMain.getName() + " Workflow");
				cActivity.setName(listActivities.get(i).getName().replaceAll(" ", "")+ "Workflow" );
				cActivity.setPackage(pack);//((Activity) (listActivities.get(i))).getPackage());
				
				
				Diagram diagramNew = null;
				try {
					
					//if(dg == null) {
						diagramNew = ModelElementsManager.getInstance()
							.createDiagram(DiagramTypeConstants.UML_ACTIVITY_DIAGRAM, cActivity);
					         Application.getInstance().getGUILog().log("addWorkflow createDiagram(DiagramTypeConstants.UML_ACTIVITY_DIAGRAM ");
					//}else {
						// diagramNew = dg;
						// Application.getInstance().getGUILog().log("addWorkflow Using dg ");
					//}
					
				}catch( Exception e){
					Application.getInstance().getGUILog().log("addWorkflow EXCEPTION  !!!!!!!!!  UML_ACTIVITY_DIAGRAM ");
					
				}
				
				//Application.getInstance().getGUILog().log("Req-> Process  addWorkflow cMain.getOwningPackage().   " + cMain.getOwningPackage().getName());
				Application.getInstance().getGUILog().log("Req-> Process  cActivity. Name   " + cActivity.getName());
				
				Iterator<ActivityNode> itNodes = ((Activity) (listActivities.get(i))).getNode().iterator();
				ArrayList<ActivityNode> listOldNodes = new ArrayList<ActivityNode>();
				ArrayList<ActivityNode> listOldNodeParents = new ArrayList<ActivityNode>();
				while (itNodes.hasNext()) {
					listOldNodes.add(itNodes.next());
					listOldNodeParents.add(null);
					//Application.getInstance().getGUILog().log("Req-> Process ########3########$$$$$$$$$$  " );
				}
				
				Iterator<ActivityEdge> itEdges = ((Activity) (listActivities.get(i))).getEdge().iterator();
				ArrayList<ActivityEdge> listEdges = new ArrayList<ActivityEdge>();
				ArrayList<ActivityNode> listEdgeParents = new ArrayList<ActivityNode>();
				while (itEdges.hasNext()) {
					listEdges.add(itEdges.next());
					listEdgeParents.add(null);
				}
               String startUserAction = "";				
				
				
		
				
				for (int q = 0; q < listOldNodes.size(); q++) {
					ActivityNode cTemp = listOldNodes.get(q);
					boolean userAction = false;
					boolean sysAction = false;
					boolean hasValidate = false;

				if (cTemp instanceof CallBehaviorAction) {
					CallBehaviorAction cOld = ((CallBehaviorAction) (cTemp));
					if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.USER_ACTION.toString())) {
						userAction = true;

					} else if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.SYSTEM_ACTION.toString())) {

						sysAction = true;

					}
					if (userAction || sysAction) {

						CallBehaviorAction cNew = Application.getInstance().getProject().getElementsFactory()
								.createCallBehaviorActionInstance();
						acNodes.put(cTemp, cNew);

						//cNew.setActivity(cOld.getActivity());
						//cNew.setBehavior(cOld.getBehavior());
						//cNew.setInStructuredNode(cOld.getInStructuredNode());
						cNew.setName(cOld.getName());
						cNew.setNameExpression(cOld.getNameExpression());
						cNew.setOnPort(cOld.getOnPort());
						cNew.setSynchronous(cOld.isSynchronous());
						cNew.setVisibility(cOld.getVisibility());
						cNew.setActivity(cActivity);

						if (listOldNodeParents.get(q) == null) {
							cNew.setOwner(cActivity);
						} else {
							cNew.setOwner(listOldNodeParents.get(q));
						}
						if (userAction) {
							
							//create user action view
							try {
								ShapeElement elShape = null;
								elShape = PresentationElementsManager.getInstance().createShapeElement(cNew,
										Application.getInstance().getProject().getDiagram(diagramNew));
								idx++;
								listShapes.add(elShape);
							} catch (Exception e) {
								Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
							}
							
							//create pin
							
							Iterator<OutputPin> itOutputPins = cOld.getOutput().iterator();
							int m =0;
							while (itOutputPins.hasNext()) {
								OutputPin outOldPin = itOutputPins.next();
								OutputPin outNewPin = Application.getInstance().getProject().getElementsFactory()
										.createOutputPinInstance();
								Application.getInstance().getGUILog().log("addWorkflow createOutputPinInstance ");
								//acNodes.put(outOldPin, outNewPin);
								m++;

								outNewPin.setActivity(outOldPin.getActivity());
								outNewPin.setControl(outOldPin.isControl());
								outNewPin.setControlType(outOldPin.isControlType());
								outNewPin.setInStructuredNode(outOldPin.getInStructuredNode());
								outNewPin.setLowerValue(outOldPin.getLowerValue());
								outNewPin.setName(outOldPin.getName());
								outNewPin.setNameExpression(outOldPin.getNameExpression());
								outNewPin.setOrdering(outOldPin.getOrdering());
								outNewPin.setParameter(outOldPin.getParameter());
								outNewPin.setSelection(outOldPin.getSelection());
								outNewPin.setType(outOldPin.getType());
								outNewPin.setUnique(outOldPin.isUnique());
								outNewPin.setUpperBound(outOldPin.getUpperBound());
								outNewPin.setUpperValue(outOldPin.getUpperValue());
								outNewPin.setVisibility(outOldPin.getVisibility());

								outNewPin.setOwner(cNew);
								
								try {
									ShapeElement elShape = null;
									elShape = PresentationElementsManager.getInstance().createShapeElement(outNewPin,
											Application.getInstance().getProject().getDiagram(diagramNew));
									//listShapes.add(elShape);
									PresentationElementsManager.getInstance().reshapeShapeElement(elShape,
											new Rectangle(0, m*6, 0, 0));
								} catch (Exception e) {
									Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
								}
							}
							if( m>3){
								listBounds.add(new Rectangle(300,idx*70 , m*20, m*20));
							}else{
								listBounds.add(new Rectangle(300,idx*70 , 0, 0));
							}
							
					
							
							//end
							
							StereotypesHelper.addStereotypeByString(cNew,
									UWEStereotypeProcessFlow.USER_ACTION.toString());
							Property property = StereotypesHelper.getPropertyByName(
									StereotypesHelper.getStereotype(Application.getInstance().getProject(),
											UWEStereotypeProcessFlow.USER_ACTION.toString()),
									UWETagUserAction.VALIDATED.toString());

							Slot slotOld = StereotypesHelper.getSlot(cOld, property, false, false);
							if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
								hasValidate = true;
								//create validate data System action
								
								
								CallBehaviorAction validateSysAction = Application.getInstance().getProject().getElementsFactory()
										.createCallBehaviorActionInstance();
								//validateSysAction.setActivity(cOld.getActivity());
								validateSysAction.setActivity(cActivity);
								
								validateSysAction.setName("Validate");
								
								validateSysAction.setVisibility(cOld.getVisibility());
								StereotypesHelper.addStereotypeByString(validateSysAction,
										UWEStereotypeProcessFlow.SYSTEM_ACTION.toString());
								
								try {
									ShapeElement elShape = null;
									elShape = PresentationElementsManager.getInstance().createShapeElement(validateSysAction,
											Application.getInstance().getProject().getDiagram(diagramNew));
									listShapes.add(elShape);
									idx++;
									listBounds.add(new Rectangle(300,idx*70 , 0, 0));
								} catch (Exception e) {
									Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
								}
								// create DecisionNode
								
								
								
								
								DecisionNode nodeTemp=Application.getInstance().getProject().getElementsFactory().createDecisionNodeInstance();

								//nodeTemp.setActivity(cOld.getActivity());
								nodeTemp.setActivity(cActivity);
								
								nodeTemp.setInStructuredNode(cOld.getInStructuredNode());
								nodeTemp.setName("Validate");
								nodeTemp.setNameExpression(cOld.getNameExpression());
								nodeTemp.setVisibility(cOld.getVisibility());

								if (listOldNodeParents.get(q) == null) {
									nodeTemp.setOwner(cActivity);
								} else {
									nodeTemp.setOwner(listOldNodeParents.get(q));
								}
								
							
        	            		
        	            		try {
        							ShapeElement elShape = null;
        							elShape = PresentationElementsManager.getInstance().createShapeElement(nodeTemp,
        									Application.getInstance().getProject().getDiagram(diagramNew));
        							listShapes.add(elShape);
        							idx++;
									listBounds.add(new Rectangle(300,idx*70 , 0, 0));
        							
        						} catch (Exception e) {
        							Application.getInstance().getGUILog().showError("Exception 455555 in ReqToP");
        						}
							}
							
						
							
							
							
						} else {
							StereotypesHelper.addStereotypeByString(cNew,
									UWEStereotypeProcessFlow.SYSTEM_ACTION.toString());
							// create system action
							try {
								ShapeElement elShape = null;
								elShape = PresentationElementsManager.getInstance().createShapeElement(cNew,
										Application.getInstance().getProject().getDiagram(diagramNew));
								listShapes.add(elShape);
								idx++;
								listBounds.add(new Rectangle(300,idx*70 , 0, 0));
							} catch (Exception e) {
								Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
							}
							// check is need confirm => create DecisionNode and user action to confirm
							Property property = StereotypesHelper.getPropertyByName(
									StereotypesHelper.getStereotype(Application.getInstance().getProject(),
											UWEStereotypeProcessFlow.SYSTEM_ACTION.toString()),
									UWETagSystemAction.CONFIRMED.toString());

							Slot slotOld = StereotypesHelper.getSlot(cOld, property, false, false);
							if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
								//create user action to confirmation
								
								CallBehaviorAction confirmUserAction = Application.getInstance().getProject().getElementsFactory()
										.createCallBehaviorActionInstance();
								//confirmUserAction.setActivity(cOld.getActivity());
								confirmUserAction.setActivity(cActivity);
								confirmUserAction.setName("Confirmation");
								
								confirmUserAction.setVisibility(cOld.getVisibility());
								StereotypesHelper.addStereotypeByString(confirmUserAction,
										UWEStereotypeProcessFlow.USER_ACTION.toString());
								
								try {
									ShapeElement elShape = null;
									elShape = PresentationElementsManager.getInstance().createShapeElement(confirmUserAction,
											Application.getInstance().getProject().getDiagram(diagramNew));
									listShapes.add(elShape);
									idx++;
									listBounds.add(new Rectangle(300,idx*70 , 0, 0));
								} catch (Exception e) {
									Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
								}
								
								
								
								
								//create DecisionNode
								DecisionNode nodeTemp=Application.getInstance().getProject().getElementsFactory().createDecisionNodeInstance();

								//nodeTemp.setActivity(cOld.getActivity());
								nodeTemp.setActivity(cActivity);
								//nodeTemp.setInStructuredNode(cOld.getInStructuredNode());
								nodeTemp.setName("Confirm");
								nodeTemp.setNameExpression(cOld.getNameExpression());
								nodeTemp.setVisibility(cOld.getVisibility());

								if (listOldNodeParents.get(q) == null) {
									nodeTemp.setOwner(cActivity);
								} else {
									nodeTemp.setOwner(listOldNodeParents.get(q));
								}
								
								try {
									ShapeElement elShape = null;
									elShape = PresentationElementsManager.getInstance().createShapeElement(nodeTemp,
											Application.getInstance().getProject().getDiagram(diagramNew));
									listShapes.add(elShape);
									idx++;
									listBounds.add(new Rectangle(300,idx*70 , 0, 0));
								} catch (Exception e) {
									Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
								}
								
								
								
								
								
							}
							
							
							
							
						}

						
					}

				}//end if Behavior Action
				 if (cTemp instanceof CentralBufferNode) {
						CentralBufferNode cOld = ((CentralBufferNode) (cTemp));
						CentralBufferNode cNew = Application.getInstance().getProject().getElementsFactory()
								.createCentralBufferNodeInstance();
						//acNodes.put(cTemp, cNew);

						//cNew.setActivity(cOld.getActivity());
						cNew.setActivity(cActivity);
					//	cNew.setInStructuredNode(cOld.getInStructuredNode());
						cNew.setName(cOld.getName());
						cNew.setNameExpression(cOld.getNameExpression());
						cNew.setVisibility(cOld.getVisibility());
						cNew.setControlType(cOld.isControlType());
						cNew.setOrdering(cOld.getOrdering());
						cNew.setSelection(cOld.getSelection());
						cNew.setType(cOld.getType());
						cNew.setUpperBound(cOld.getUpperBound());

						cNew.setName(cOld.getName());
						if (listOldNodeParents.get(q) == null) {
							cNew.setOwner(cActivity);
						} else {
							cNew.setOwner(listOldNodeParents.get(q));
						}
						
						try {
							ShapeElement elShape = null;
							elShape = PresentationElementsManager.getInstance().createShapeElement(cNew,
									Application.getInstance().getProject().getDiagram(diagramNew));
							listShapes.add(elShape);
							idx++;
							listBounds.add(new Rectangle(300,idx*70 , 0, 0));
						} catch (Exception e) {
							Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
						}
						
						
						
				 }
				
				 
				 
				 
				 
				 
			}//end for
			
				
				 //create final activity node
				 
				ActivityFinalNode cNew = Application.getInstance().getProject().getElementsFactory()
						.createActivityFinalNodeInstance();
				Application.getInstance().getGUILog().log("addWorkflow createActivityFinalNodeInstance ");
				//acNodes.put(cTemp, cNew);

				cNew.setActivity(cActivity);
				//cNew.setInStructuredNode(cOld.getInStructuredNode());
				cNew.setName("Final");
				//cNew.setNameExpression(cOld.getNameExpression());
				//cNew.setVisibility(cOld.getVisibility());
				cNew.setOwner(cActivity);

				
				try {
					ShapeElement elShape = null;
					elShape = PresentationElementsManager.getInstance().createShapeElement(cNew,
							Application.getInstance().getProject().getDiagram(diagramNew));
					listShapes.add(elShape);
					idx++;
					listBounds.add(new Rectangle(300,idx*70 , 0, 0));
				} catch (Exception e) {
					Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
				}
				
				
			 //relayout shape
				for(int k =0; k < listShapes.size(); k++){
					ShapeElement shape = listShapes.get(k);
					try{
							PresentationElementsManager.getInstance().reshapeShapeElement(shape,
							new Rectangle(300, (k+1)*70, 100, 80));
							//Rectangle rect = listBounds.get(k);
							//PresentationElementsManager.getInstance().reshapeShapeElement(shape,
								//	rect);
							//rect.getY();
							
					}catch(Exception e){
						Application.getInstance().getGUILog().showError("Exception 4444 in ReqToP");
					}
					
				}
				
				
				
			
			if ((bCreated) && (SessionManager.getInstance().isSessionCreated())) {
				SessionManager.getInstance().closeSession();
			}

				
		 }//end for activity
		 
		 
		
		 
		 
		 
		 return true;
	 }
	  
	public static boolean addWorkflow(Class cMain) {
		Application.getInstance().getGUILog().log(" Req-> Process addWorkflow (+)");
		ArrayList<Action> nodesRemove = new ArrayList<Action>();
		ArrayList<ArrayList<ShapeElement>> shapeActionPins = new ArrayList<ArrayList<ShapeElement>>();
		ArrayList<NamedElement> listReqModels = ElementCollector
				.getNamedElements(UWEDiagramType.USE_CASE.modelStereotype, null, Model.class, false, true);
		ArrayList<NamedElement> temp = ElementCollector.getNamedElements(
				UWEStereotypeRequirementsUseCases.PROCESSING_USECASE.toString(), null, UseCase.class, false,
				listReqModels, false);
		ArrayList<NamedElement> temp2 = ElementCollector.getNamedElements(
				UWEStereotypeRequirementsUseCases.PROCESSING_PACKAGE.toString(), null, Package.class, false,
				listReqModels, false);
		ArrayList<NamedElement> temp3 = ElementCollector.getNamedElements(null, null, UseCase.class, false, temp2,
				false);
		temp.addAll(temp3);

		ArrayList<NamedElement> listConModels = ElementCollector
				.getNamedElements(UWEDiagramType.CONTENT.modelStereotype, null, Model.class, false, true);
		ArrayList<NamedElement> listClasses = ElementCollector.getNamedElements(null, null, Class.class, false,
				listConModels, false);
		ArrayList<NamedElement> listUserModels = ElementCollector
				.getNamedElements(UWEDiagramType.USER_MODEL.modelStereotype, null, Model.class, false, true);
		ArrayList<NamedElement> listClasses2 = ElementCollector.getNamedElements(null, null, Class.class, false,
				listUserModels, false);
		listClasses.addAll(listClasses2);

		ArrayList<NamedElement> listUseCases = new ArrayList<NamedElement>();
		if (ElementCollector.getNamedElementFromArrayList(temp, cMain.getName(), false,
				true) != ElementCollector.iNO_ELEMENT) {
			listUseCases
					.add(temp.get(ElementCollector.getNamedElementFromArrayList(temp, cMain.getName(), false, true)));
			Application.getInstance().getGUILog().log("Req-> Process  listUseCases add new Item");
		} else {
			Application.getInstance().getGUILog().log("Req-> Process  listUseCases RETURN FALSE");
			return (false);
		}
		
		
		 for(int i =0; i < listUseCases.size(); i++) {
	        	Application.getInstance().getGUILog().log("Req-> Process  listUseCases name : " + listUseCases.get(i).getName());
	        }
		
		
		 ArrayList<NamedElement> listReqModels_ = ElementCollector
					.getNamedElements(UWEDiagramType.USE_CASE.modelStereotype, null, Model.class, false, true);
		 
		/* for(int i =0; i < listReqModels_.size(); i++) {
	        	Application.getInstance().getGUILog().log("Req-> Process  listReqModels name : " + listReqModels_.get(i).getName());
	        }*/
		ArrayList<NamedElement> listActivities = new ArrayList<NamedElement>();
		ArrayList<NamedElement> listActivitiesA = ElementCollector.getNamedElements(null, null, Activity.class, true,
				listReqModels_, false);
		 for(int i =0; i < listActivitiesA.size(); i++) {
			 if(listActivitiesA.get(i).getName().equals(listUseCases.get(0).getName())) {
				 listActivities.add(listActivitiesA.get(i));
			 }
	        	//Application.getInstance().getGUILog().log("MagicUWE: Requirements -> Presentation addPresentationChildren listActivitiesA name : " + listActivitiesA.get(i).getName());
	        }
		
		
		/*ArrayList<NamedElement> listActivities = ElementCollector.getNamedElements(null, null, Activity.class, true,
				listUseCases, false);*/
		 
	// listActivities = ElementCollector.getNamedElements(null, null, Activity.class, true,
				// cMain, false);

		ArrayList<ShapeElement> listShapes = new ArrayList<ShapeElement>();
		ArrayList<PresentationElement> listMasters = new ArrayList<PresentationElement>();
		Application.getInstance().getGUILog().log("Req-> Process  addWorkflow listActivities SIZE   " + listActivities.size());
		for (int i = 0; i < listActivities.size(); i++) {
			Application.getInstance().getGUILog().log("Req-> Process  addWorkflow listActivities   " + listActivities.get(i).getName());
			boolean bCreated = false;
			if (!SessionManager.getInstance().isSessionCreated()) {
				bCreated = true;
				SessionManager.getInstance().createSession("Create Workflow");
			}

			HashMap<ActivityNode, ActivityNode> acNodes = new HashMap<ActivityNode, ActivityNode>();
			HashMap<ActivityEdge, ActivityEdge> acEdges = new HashMap<ActivityEdge, ActivityEdge>();

			Activity cActivity = Application.getInstance().getProject().getElementsFactory().createActivityInstance();
			UWEDiagramType dgType = UWEDiagramType.PROCESS_FLOW;
			 
			
				Package pack =
						dgType.getModelOrCreateIt(Application.getInstance().getProjectsManager()// create process package
								.getActiveProject(),
								"Do you want the new diagram(s) to be stored in a new model called \""
										+ dgType.originalModelName + "\"\nincluding the stereotype \""
										+ dgType.modelStereotype
										+ "\"? (recommended) \n\n(\"No\" will ask you to select a package)",
								true);
		
		
			cActivity.setName(listActivities.get(i).getName().replaceAll(" ", "")+ "Workflow" );
			cActivity.setPackage(pack);//((Activity) (listActivities.get(i))).getPackage());
			/*cActivity.setAbstract(((Activity) (listActivities.get(i))).isAbstract());
			cActivity.setActive(((Activity) (listActivities.get(i))).isActive());
			cActivity.setAppliedStereotypeInstance(listActivities.get(i).getAppliedStereotypeInstance());
			cActivity.setClassifierBehavior(((Activity) (listActivities.get(i))).getClassifierBehavior());
			cActivity.setNameExpression(listActivities.get(i).getNameExpression());
			cActivity.setNamespace(listActivities.get(i).getNamespace());
			cActivity.setOwnedTemplateSignature(((Activity) (listActivities.get(i))).getOwnedTemplateSignature());
			cActivity.setOwningPackage(cMain.getOwningPackage());
			cActivity.setOwningTemplateParameter(((Activity) (listActivities.get(i))).getOwningTemplateParameter());
			cActivity.setPackage(pack);//((Activity) (listActivities.get(i))).getPackage());
			cActivity.setReadOnly(((Activity) (listActivities.get(i))).isReadOnly());
			cActivity.setReentrant(((Activity) (listActivities.get(i))).isReentrant());
			cActivity.setRepresentation(((Activity) (listActivities.get(i))).getRepresentation());
			cActivity.setSingleExecution(((Activity) (listActivities.get(i))).isSingleExecution());
			cActivity.setTemplateParameter(((Activity) (listActivities.get(i))).getTemplateParameter());
			cActivity.setSpecification(((Activity) (listActivities.get(i))).getSpecification());
			cActivity.setUMLClass(((Activity) (listActivities.get(i))).getUMLClass());
			cActivity.setVisibility(listActivities.get(i).getVisibility());
			cActivity.setOwner(cMain);*/
			
			Application.getInstance().getGUILog().log("Req-> Process  addWorkflow cMain.getOwningPackage().   " + cMain.getOwningPackage().getName());
			Application.getInstance().getGUILog().log("Req-> Process  cActivity. Name   " + cActivity.getName());

			Iterator<ActivityNode> itNodes = ((Activity) (listActivities.get(i))).getNode().iterator();
			ArrayList<ActivityNode> listOldNodes = new ArrayList<ActivityNode>();
			ArrayList<ActivityNode> listOldNodeParents = new ArrayList<ActivityNode>();
			while (itNodes.hasNext()) {
				listOldNodes.add(itNodes.next());
				listOldNodeParents.add(null);
			}
			Iterator<ActivityEdge> itEdges = ((Activity) (listActivities.get(i))).getEdge().iterator();
			ArrayList<ActivityEdge> listEdges = new ArrayList<ActivityEdge>();
			ArrayList<ActivityNode> listEdgeParents = new ArrayList<ActivityNode>();
			while (itEdges.hasNext()) {
				listEdges.add(itEdges.next());
				listEdgeParents.add(null);
			}

			for (int q = 0; q < listOldNodes.size(); q++) {
				ActivityNode cTemp = listOldNodes.get(q);

				if (cTemp instanceof CallBehaviorAction) {
					CallBehaviorAction cOld = ((CallBehaviorAction) (cTemp));
					CallBehaviorAction cNew = Application.getInstance().getProject().getElementsFactory()
							.createCallBehaviorActionInstance();
					acNodes.put(cTemp, cNew);

					cNew.setActivity(cOld.getActivity());
					cNew.setBehavior(cOld.getBehavior());
					cNew.setInStructuredNode(cOld.getInStructuredNode());
					cNew.setName(cOld.getName());
					cNew.setNameExpression(cOld.getNameExpression());
					cNew.setOnPort(cOld.getOnPort());
					cNew.setSynchronous(cOld.isSynchronous());
					cNew.setVisibility(cOld.getVisibility());

					if (listOldNodeParents.get(q) == null) {
						cNew.setOwner(cActivity);
					} else {
						cNew.setOwner(listOldNodeParents.get(q));
					}

					if (StereotypesHelper.hasStereotype(cOld,
							UWEStereotypeRequirementsActions.DISPLAY_ACTION.toString())) {
						StereotypesHelper.addStereotypeByString(cNew,
								UWEStereotypeRequirementsActions.DISPLAY_ACTION.toString());
						nodesRemove.add(cNew);
					} else if (StereotypesHelper.hasStereotype(cOld,
							UWEStereotypeProcessFlow.SYSTEM_ACTION.toString())) {
						StereotypesHelper.addStereotypeByString(cNew,
								UWEStereotypeProcessFlow.SYSTEM_ACTION.toString());

						Property property = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(Application.getInstance().getProject(),
										UWEStereotypeProcessFlow.SYSTEM_ACTION.toString()),
								UWETagSystemAction.CONFIRMED.toString());

						Slot slotOld = StereotypesHelper.getSlot(cOld, property, false, false);
						if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
							Slot slotNew = StereotypesHelper.getSlot(cNew, property, true, false);
							LiteralBoolean litBoolean = Application.getInstance().getProject().getElementsFactory()
									.createLiteralBooleanInstance();
							litBoolean.setValue(((LiteralBoolean) (slotOld.getValue().get(0))).isValue());
							slotNew.getValue().add(litBoolean);
						}
					} else if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.USER_ACTION.toString())) {
						StereotypesHelper.addStereotypeByString(cNew, UWEStereotypeProcessFlow.USER_ACTION.toString());

						Property property = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(Application.getInstance().getProject(),
										UWEStereotypeProcessFlow.USER_ACTION.toString()),
								UWETagUserAction.VALIDATED.toString());

						Slot slotOld = StereotypesHelper.getSlot(cOld, property, false, false);
						if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
							Slot slotNew = StereotypesHelper.getSlot(cNew, property, true, false);
							LiteralBoolean litBoolean = Application.getInstance().getProject().getElementsFactory()
									.createLiteralBooleanInstance();
							litBoolean.setValue(((LiteralBoolean) (slotOld.getValue().get(0))).isValue());
							slotNew.getValue().add(litBoolean);
						}
					} else if (StereotypesHelper.hasStereotype(cOld,
							UWEStereotypeRequirementsActions.NAVIGATION_ACTION.toString())) {
						StereotypesHelper.addStereotypeByString(cNew,
								UWEStereotypeRequirementsActions.NAVIGATION_ACTION.toString());
						nodesRemove.add(cNew);
					}

					Iterator<InputPin> itInputPins = cOld.getInput().iterator();
					while (itInputPins.hasNext()) {
						InputPin inOldPin = itInputPins.next();
						InputPin inNewPin = Application.getInstance().getProject().getElementsFactory()
								.createInputPinInstance();
						Application.getInstance().getGUILog().log("addWorkflow createInputPinInstance ");
						acNodes.put(inOldPin, inNewPin);

						inNewPin.setActivity(inOldPin.getActivity());
						inNewPin.setControl(inOldPin.isControl());
						inNewPin.setControlType(inOldPin.isControlType());
						inNewPin.setInStructuredNode(inOldPin.getInStructuredNode());
						inNewPin.setLowerValue(inOldPin.getLowerValue());
						inNewPin.setName(inOldPin.getName());
						inNewPin.setNameExpression(inOldPin.getNameExpression());
						inNewPin.setOrdering(inOldPin.getOrdering());
						inNewPin.setParameter(inOldPin.getParameter());
						inNewPin.setSelection(inOldPin.getSelection());
						inNewPin.setType(inOldPin.getType());
						inNewPin.setUnique(inOldPin.isUnique());
						inNewPin.setUpperBound(inOldPin.getUpperBound());
						inNewPin.setUpperValue(inOldPin.getUpperValue());
						inNewPin.setVisibility(inOldPin.getVisibility());

						inNewPin.setOwner(cNew);
					}

					Iterator<OutputPin> itOutputPins = cOld.getOutput().iterator();
					while (itOutputPins.hasNext()) {
						OutputPin outOldPin = itOutputPins.next();
						OutputPin outNewPin = Application.getInstance().getProject().getElementsFactory()
								.createOutputPinInstance();
						Application.getInstance().getGUILog().log("addWorkflow createOutputPinInstance ");
						acNodes.put(outOldPin, outNewPin);

						outNewPin.setActivity(outOldPin.getActivity());
						outNewPin.setControl(outOldPin.isControl());
						outNewPin.setControlType(outOldPin.isControlType());
						outNewPin.setInStructuredNode(outOldPin.getInStructuredNode());
						outNewPin.setLowerValue(outOldPin.getLowerValue());
						outNewPin.setName(outOldPin.getName());
						outNewPin.setNameExpression(outOldPin.getNameExpression());
						outNewPin.setOrdering(outOldPin.getOrdering());
						outNewPin.setParameter(outOldPin.getParameter());
						outNewPin.setSelection(outOldPin.getSelection());
						outNewPin.setType(outOldPin.getType());
						outNewPin.setUnique(outOldPin.isUnique());
						outNewPin.setUpperBound(outOldPin.getUpperBound());
						outNewPin.setUpperValue(outOldPin.getUpperValue());
						outNewPin.setVisibility(outOldPin.getVisibility());

						outNewPin.setOwner(cNew);
					}
				} else if (cTemp instanceof StructuredActivityNode) {
					StructuredActivityNode cOld = ((StructuredActivityNode) (cTemp));
					StructuredActivityNode cNew = Application.getInstance().getProject().getElementsFactory()
							.createStructuredActivityNodeInstance();
					acNodes.put(cTemp, cNew);

					cNew.setActivity(cOld.getActivity());
					cNew.setInStructuredNode(cOld.getInStructuredNode());
					cNew.setName(cOld.getName());
					cNew.setNameExpression(cOld.getNameExpression());
					cNew.setVisibility(cOld.getVisibility());

					if (listOldNodeParents.get(q) == null) {
						cNew.setOwner(cActivity);
					} else {
						cNew.setOwner(listOldNodeParents.get(q));
					}

					if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.SYSTEM_ACTION.toString())) {
						StereotypesHelper.addStereotypeByString(cNew,
								UWEStereotypeProcessFlow.SYSTEM_ACTION.toString());

						Property property = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(Application.getInstance().getProject(),
										UWEStereotypeProcessFlow.SYSTEM_ACTION.toString()),
								UWETagSystemAction.CONFIRMED.toString());

						Slot slotOld = StereotypesHelper.getSlot(cOld, property, false, false);
						if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
							Slot slotNew = StereotypesHelper.getSlot(cNew, property, true, false);
							LiteralBoolean litBoolean = Application.getInstance().getProject().getElementsFactory()
									.createLiteralBooleanInstance();
							litBoolean.setValue(((LiteralBoolean) (slotOld.getValue().get(0))).isValue());
							slotNew.getValue().add(litBoolean);
						}
					} else if (StereotypesHelper.hasStereotype(cOld, UWEStereotypeProcessFlow.USER_ACTION.toString())) {
						StereotypesHelper.addStereotypeByString(cNew, UWEStereotypeProcessFlow.USER_ACTION.toString());

						Property property = StereotypesHelper.getPropertyByName(
								StereotypesHelper.getStereotype(Application.getInstance().getProject(),
										UWEStereotypeProcessFlow.USER_ACTION.toString()),
								UWETagUserAction.VALIDATED.toString());

						Slot slotOld = StereotypesHelper.getSlot(cOld, property, false, false);
						if ((slotOld != null) && (slotOld.getValue().size() > 0)) {
							Slot slotNew = StereotypesHelper.getSlot(cNew, property, true, false);
							LiteralBoolean litBoolean = Application.getInstance().getProject().getElementsFactory()
									.createLiteralBooleanInstance();
							litBoolean.setValue(((LiteralBoolean) (slotOld.getValue().get(0))).isValue());
							slotNew.getValue().add(litBoolean);
						}
					}

					Iterator<InputPin> itInputPins = cOld.getInput().iterator();
					while (itInputPins.hasNext()) {
						InputPin inOldPin = itInputPins.next();
						InputPin inNewPin = Application.getInstance().getProject().getElementsFactory()
								.createInputPinInstance();
						acNodes.put(inOldPin, inNewPin);

						inNewPin.setActivity(inOldPin.getActivity());
						inNewPin.setControl(inOldPin.isControl());
						inNewPin.setControlType(inOldPin.isControlType());
						inNewPin.setInStructuredNode(inOldPin.getInStructuredNode());
						inNewPin.setLowerValue(inOldPin.getLowerValue());
						inNewPin.setName(inOldPin.getName());
						inNewPin.setNameExpression(inOldPin.getNameExpression());
						inNewPin.setOrdering(inOldPin.getOrdering());
						inNewPin.setParameter(inOldPin.getParameter());
						inNewPin.setSelection(inOldPin.getSelection());
						inNewPin.setType(inOldPin.getType());
						inNewPin.setUnique(inOldPin.isUnique());
						inNewPin.setUpperBound(inOldPin.getUpperBound());
						inNewPin.setUpperValue(inOldPin.getUpperValue());
						inNewPin.setVisibility(inOldPin.getVisibility());

						inNewPin.setOwner(cNew);
					}

					Iterator<OutputPin> itOutputPins = cOld.getOutput().iterator();
					while (itOutputPins.hasNext()) {
						OutputPin outOldPin = itOutputPins.next();
						OutputPin outNewPin = Application.getInstance().getProject().getElementsFactory()
								.createOutputPinInstance();
						acNodes.put(outOldPin, outNewPin);

						outNewPin.setActivity(outOldPin.getActivity());
						outNewPin.setControl(outOldPin.isControl());
						outNewPin.setControlType(outOldPin.isControlType());
						outNewPin.setInStructuredNode(outOldPin.getInStructuredNode());
						outNewPin.setLowerValue(outOldPin.getLowerValue());
						outNewPin.setName(outOldPin.getName());
						outNewPin.setNameExpression(outOldPin.getNameExpression());
						outNewPin.setOrdering(outOldPin.getOrdering());
						outNewPin.setParameter(outOldPin.getParameter());
						outNewPin.setSelection(outOldPin.getSelection());
						outNewPin.setType(outOldPin.getType());
						outNewPin.setUnique(outOldPin.isUnique());
						outNewPin.setUpperBound(outOldPin.getUpperBound());
						outNewPin.setUpperValue(outOldPin.getUpperValue());
						outNewPin.setVisibility(outOldPin.getVisibility());

						outNewPin.setOwner(cNew);
					}

					Iterator<ActivityNode> itInnerNodes = cOld.getNode().iterator();
					while (itInnerNodes.hasNext()) {
						listOldNodes.add(itInnerNodes.next());
						listOldNodeParents.add(cNew);
					}

					Iterator<ActivityEdge> itInnerEdges = cOld.getEdge().iterator();
					while (itInnerEdges.hasNext()) {
						listEdges.add(itInnerEdges.next());
						listEdgeParents.add(cNew);
					}

					if (cNew.getOwnedElement().isEmpty()) {
						nodesRemove.add(cNew);
					}
				} else if (cTemp instanceof InitialNode) {
					InitialNode cOld = ((InitialNode) (cTemp));
					InitialNode cNew = Application.getInstance().getProject().getElementsFactory()
							.createInitialNodeInstance();
					acNodes.put(cTemp, cNew);

					cNew.setActivity(cOld.getActivity());
					cNew.setInStructuredNode(cOld.getInStructuredNode());
					cNew.setName(cOld.getName());
					cNew.setNameExpression(cOld.getNameExpression());
					cNew.setVisibility(cOld.getVisibility());

					if (listOldNodeParents.get(q) == null) {
						cNew.setOwner(cActivity);
					} else {
						cNew.setOwner(listOldNodeParents.get(q));
					}
				} else if (cTemp instanceof ActivityFinalNode) {
					ActivityFinalNode cOld = ((ActivityFinalNode) (cTemp));
					ActivityFinalNode cNew = Application.getInstance().getProject().getElementsFactory()
							.createActivityFinalNodeInstance();
					Application.getInstance().getGUILog().log("addWorkflow createActivityFinalNodeInstance ");
					acNodes.put(cTemp, cNew);

					cNew.setActivity(cOld.getActivity());
					cNew.setInStructuredNode(cOld.getInStructuredNode());
					cNew.setName(cOld.getName());
					cNew.setNameExpression(cOld.getNameExpression());
					cNew.setVisibility(cOld.getVisibility());

					if (listOldNodeParents.get(q) == null) {
						cNew.setOwner(cActivity);
					} else {
						cNew.setOwner(listOldNodeParents.get(q));
					}
				} else if (cTemp instanceof CentralBufferNode) {
					CentralBufferNode cOld = ((CentralBufferNode) (cTemp));
					CentralBufferNode cNew = Application.getInstance().getProject().getElementsFactory()
							.createCentralBufferNodeInstance();
					acNodes.put(cTemp, cNew);

					cNew.setActivity(cOld.getActivity());
					cNew.setInStructuredNode(cOld.getInStructuredNode());
					cNew.setName(cOld.getName());
					cNew.setNameExpression(cOld.getNameExpression());
					cNew.setVisibility(cOld.getVisibility());
					cNew.setControlType(cOld.isControlType());
					cNew.setOrdering(cOld.getOrdering());
					cNew.setSelection(cOld.getSelection());
					cNew.setType(cOld.getType());
					cNew.setUpperBound(cOld.getUpperBound());

					if ((cOld.getType() != null) && (ElementCollector.getNamedElementFromArrayList(listClasses,
							cOld.getType().getName(), false, false) != ElementCollector.iNO_ELEMENT)) {
						cNew.setType(((Class) (listClasses.get(ElementCollector
								.getNamedElementFromArrayList(listClasses, cOld.getType().getName(), false, false)))));
					} else if ((cOld.getType() == null) && (ElementCollector.getNamedElementFromArrayList(listClasses,
							cOld.getName(), false, false) != ElementCollector.iNO_ELEMENT)) {
						cNew.setType(((Class) (listClasses.get(ElementCollector
								.getNamedElementFromArrayList(listClasses, cOld.getName(), false, false)))));
					}

					cNew.setName(cOld.getName());
					if (listOldNodeParents.get(q) == null) {
						cNew.setOwner(cActivity);
					} else {
						cNew.setOwner(listOldNodeParents.get(q));
					}
				} else if (cTemp instanceof DecisionNode) {
					DecisionNode cOld = ((DecisionNode) (cTemp));
					DecisionNode cNew = Application.getInstance().getProject().getElementsFactory()
							.createDecisionNodeInstance();
					acNodes.put(cTemp, cNew);

					cNew.setActivity(cOld.getActivity());
					cNew.setInStructuredNode(cOld.getInStructuredNode());
					cNew.setName(cOld.getName());
					cNew.setNameExpression(cOld.getNameExpression());
					cNew.setVisibility(cOld.getVisibility());

					if (listOldNodeParents.get(q) == null) {
						cNew.setOwner(cActivity);
					} else {
						cNew.setOwner(listOldNodeParents.get(q));
					}
				}
			}

			for (int e = 0; e < listEdges.size(); e++) {
				if (listEdges.get(e) instanceof ObjectFlow) {
					ObjectFlow flowOld = ((ObjectFlow) (listEdges.get(e)));

					if ((flowOld.getTarget() instanceof Action)
							&& ((StereotypesHelper.hasStereotype(flowOld.getTarget(),
									UWEStereotypeRequirementsActions.DISPLAY_ACTION.toString()))
									|| (StereotypesHelper.hasStereotype(flowOld.getTarget(),
											UWEStereotypeRequirementsActions.NAVIGATION_ACTION.toString())))) {
						Iterator<ActivityEdge> itTemp = ((Activity) (listActivities.get(i))).getEdge().iterator();
						while (itTemp.hasNext()) {
							ActivityEdge tempEdge = itTemp.next();
							if (tempEdge instanceof ControlFlow) {
								ControlFlow flowOld2 = ((ControlFlow) (tempEdge));
								if (flowOld2.getSource() == flowOld.getTarget()) {
									ObjectFlow flowNew = Application.getInstance().getProject().getElementsFactory()
											.createObjectFlowInstance();
									acEdges.put(listEdges.get(e), flowNew);

									flowNew.setActivity(cActivity);
									flowNew.setGuard(flowOld.getGuard());
									flowNew.setInStructuredNode(flowOld.getInStructuredNode());
									flowNew.setInterrupts(flowOld.getInterrupts());
									flowNew.setMulticast(flowOld.isMulticast());
									flowNew.setMultireceive(flowOld.isMultireceive());
									flowNew.setName(flowOld.getName());
									flowNew.setNameExpression(flowOld.getNameExpression());
									flowNew.setSelection(flowOld.getSelection());
									flowNew.setSource(acNodes.get(flowOld.getSource()));
									flowNew.setTarget(acNodes.get(flowOld2.getTarget()));
									flowNew.setTransformation(flowOld.getTransformation());
									flowNew.setVisibility(flowOld.getVisibility());
									flowNew.setWeight(flowOld.getWeight());
									flowNew.setOwner(cActivity);
								}
							} else if (tempEdge instanceof ObjectFlow) {
								ObjectFlow flowOld2 = ((ObjectFlow) (tempEdge));
								if (flowOld2.getSource() == flowOld.getTarget()) {
									ObjectFlow flowNew = Application.getInstance().getProject().getElementsFactory()
											.createObjectFlowInstance();
									acEdges.put(listEdges.get(e), flowNew);

									flowNew.setActivity(cActivity);
									flowNew.setGuard(flowOld.getGuard());
									flowNew.setInStructuredNode(flowOld.getInStructuredNode());
									flowNew.setInterrupts(flowOld.getInterrupts());
									flowNew.setMulticast(flowOld2.isMulticast());
									flowNew.setMultireceive(flowOld2.isMultireceive());
									flowNew.setName(flowOld.getName());
									flowNew.setNameExpression(flowOld.getNameExpression());
									flowNew.setSelection(flowOld2.getSelection());
									flowNew.setSource(acNodes.get(flowOld.getSource()));
									flowNew.setTarget(acNodes.get(flowOld2.getTarget()));
									flowNew.setTransformation(flowOld2.getTransformation());
									flowNew.setVisibility(flowOld.getVisibility());
									flowNew.setWeight(flowOld.getWeight());
									flowNew.setOwner(cActivity);
								}
							}
						}
					} else if ((flowOld.getTarget() instanceof Pin)
							&& ((StereotypesHelper.hasStereotype(flowOld.getTarget().getOwner(),
									UWEStereotypeRequirementsActions.DISPLAY_ACTION.toString()))
									|| (StereotypesHelper.hasStereotype(flowOld.getTarget().getOwner(),
											UWEStereotypeRequirementsActions.NAVIGATION_ACTION.toString())))) {
						for (int e2 = 0; e2 < listEdges.size(); e2++) {
							if (e2 == e) {
								continue;
							}

							if (listEdges.get(e2) instanceof ControlFlow) {
								ControlFlow flowOld2 = ((ControlFlow) (listEdges.get(e2)));
								if (flowOld2.getSource() == flowOld.getTarget().getOwner()) {
									Pin cPin = null;
									if (flowOld.getTarget() instanceof InputPin) {
										InputPin inOldPin = ((InputPin) (flowOld.getTarget()));
										InputPin inNewPin = Application.getInstance().getProject().getElementsFactory()
												.createInputPinInstance();
										acNodes.put(inOldPin, inNewPin);

										inNewPin.setActivity(inOldPin.getActivity());
										inNewPin.setControl(inOldPin.isControl());
										inNewPin.setControlType(inOldPin.isControlType());
										inNewPin.setInStructuredNode(inOldPin.getInStructuredNode());
										inNewPin.setLowerValue(inOldPin.getLowerValue());
										inNewPin.setName(inOldPin.getName());
										inNewPin.setNameExpression(inOldPin.getNameExpression());
										inNewPin.setOrdering(inOldPin.getOrdering());
										inNewPin.setParameter(inOldPin.getParameter());
										inNewPin.setSelection(inOldPin.getSelection());
										inNewPin.setType(inOldPin.getType());
										inNewPin.setUnique(inOldPin.isUnique());
										inNewPin.setUpperBound(inOldPin.getUpperBound());
										inNewPin.setUpperValue(inOldPin.getUpperValue());
										inNewPin.setVisibility(inOldPin.getVisibility());

										inNewPin.setOwner(acNodes.get(flowOld2.getTarget()));
										cPin = inNewPin;
									} else if (flowOld.getTarget() instanceof OutputPin) {
										OutputPin outOldPin = ((OutputPin) (flowOld.getTarget()));
										OutputPin outNewPin = Application.getInstance().getProject()
												.getElementsFactory().createOutputPinInstance();
										acNodes.put(outOldPin, outNewPin);

										outNewPin.setActivity(outOldPin.getActivity());
										outNewPin.setControl(outOldPin.isControl());
										outNewPin.setControlType(outOldPin.isControlType());
										outNewPin.setInStructuredNode(outOldPin.getInStructuredNode());
										outNewPin.setLowerValue(outOldPin.getLowerValue());
										outNewPin.setName(outOldPin.getName());
										outNewPin.setNameExpression(outOldPin.getNameExpression());
										outNewPin.setOrdering(outOldPin.getOrdering());
										outNewPin.setParameter(outOldPin.getParameter());
										outNewPin.setSelection(outOldPin.getSelection());
										outNewPin.setType(outOldPin.getType());
										outNewPin.setUnique(outOldPin.isUnique());
										outNewPin.setUpperBound(outOldPin.getUpperBound());
										outNewPin.setUpperValue(outOldPin.getUpperValue());
										outNewPin.setVisibility(outOldPin.getVisibility());

										outNewPin.setOwner(acNodes.get(flowOld2.getTarget()));
										cPin = outNewPin;
									}

									ObjectFlow flowNew = Application.getInstance().getProject().getElementsFactory()
											.createObjectFlowInstance();
									acEdges.put(listEdges.get(e), flowNew);

									flowNew.setActivity(cActivity);
									flowNew.setGuard(flowOld.getGuard());
									flowNew.setInStructuredNode(flowOld.getInStructuredNode());
									flowNew.setInterrupts(flowOld.getInterrupts());
									flowNew.setMulticast(flowOld.isMulticast());
									flowNew.setMultireceive(flowOld.isMultireceive());
									flowNew.setName(flowOld.getName());
									flowNew.setNameExpression(flowOld.getNameExpression());
									flowNew.setSelection(flowOld.getSelection());
									flowNew.setSource(acNodes.get(flowOld.getSource()));
									flowNew.setTarget(cPin);
									flowNew.setTransformation(flowOld.getTransformation());
									flowNew.setVisibility(flowOld.getVisibility());
									flowNew.setWeight(flowOld.getWeight());
									flowNew.setOwner(cActivity);
								}
							} else if (listEdges.get(e2) instanceof ObjectFlow) {
								ObjectFlow flowOld2 = ((ObjectFlow) (listEdges.get(e2)));
								if (flowOld2.getSource() == flowOld.getTarget().getOwner()) {
									Pin cPin = null;
									if (flowOld.getTarget() instanceof InputPin) {
										InputPin inOldPin = ((InputPin) (flowOld.getTarget()));
										InputPin inNewPin = Application.getInstance().getProject().getElementsFactory()
												.createInputPinInstance();
										acNodes.put(inOldPin, inNewPin);

										inNewPin.setActivity(inOldPin.getActivity());
										inNewPin.setControl(inOldPin.isControl());
										inNewPin.setControlType(inOldPin.isControlType());
										inNewPin.setInStructuredNode(inOldPin.getInStructuredNode());
										inNewPin.setLowerValue(inOldPin.getLowerValue());
										inNewPin.setName(inOldPin.getName());
										inNewPin.setNameExpression(inOldPin.getNameExpression());
										inNewPin.setOrdering(inOldPin.getOrdering());
										inNewPin.setParameter(inOldPin.getParameter());
										inNewPin.setSelection(inOldPin.getSelection());
										inNewPin.setType(inOldPin.getType());
										inNewPin.setUnique(inOldPin.isUnique());
										inNewPin.setUpperBound(inOldPin.getUpperBound());
										inNewPin.setUpperValue(inOldPin.getUpperValue());
										inNewPin.setVisibility(inOldPin.getVisibility());

										inNewPin.setOwner(acNodes.get(flowOld2.getTarget()));
										cPin = inNewPin;
									} else if (flowOld.getTarget() instanceof OutputPin) {
										OutputPin outOldPin = ((OutputPin) (flowOld.getTarget()));
										OutputPin outNewPin = Application.getInstance().getProject()
												.getElementsFactory().createOutputPinInstance();
										acNodes.put(outOldPin, outNewPin);

										outNewPin.setActivity(outOldPin.getActivity());
										outNewPin.setControl(outOldPin.isControl());
										outNewPin.setControlType(outOldPin.isControlType());
										outNewPin.setInStructuredNode(outOldPin.getInStructuredNode());
										outNewPin.setLowerValue(outOldPin.getLowerValue());
										outNewPin.setName(outOldPin.getName());
										outNewPin.setNameExpression(outOldPin.getNameExpression());
										outNewPin.setOrdering(outOldPin.getOrdering());
										outNewPin.setParameter(outOldPin.getParameter());
										outNewPin.setSelection(outOldPin.getSelection());
										outNewPin.setType(outOldPin.getType());
										outNewPin.setUnique(outOldPin.isUnique());
										outNewPin.setUpperBound(outOldPin.getUpperBound());
										outNewPin.setUpperValue(outOldPin.getUpperValue());
										outNewPin.setVisibility(outOldPin.getVisibility());

										outNewPin.setOwner(acNodes.get(flowOld2.getTarget()));
										cPin = outNewPin;
									}

									ObjectFlow flowNew = Application.getInstance().getProject().getElementsFactory()
											.createObjectFlowInstance();
									acEdges.put(listEdges.get(e), flowNew);

									flowNew.setActivity(cActivity);
									flowNew.setGuard(flowOld.getGuard());
									flowNew.setInStructuredNode(flowOld.getInStructuredNode());
									flowNew.setInterrupts(flowOld.getInterrupts());
									flowNew.setMulticast(flowOld2.isMulticast());
									flowNew.setMultireceive(flowOld2.isMultireceive());
									flowNew.setName(flowOld.getName());
									flowNew.setNameExpression(flowOld.getNameExpression());
									flowNew.setSelection(flowOld2.getSelection());
									flowNew.setSource(acNodes.get(flowOld.getSource()));
									flowNew.setTarget(cPin);
									flowNew.setTransformation(flowOld2.getTransformation());
									flowNew.setVisibility(flowOld.getVisibility());
									flowNew.setWeight(flowOld.getWeight());
									flowNew.setOwner(cActivity);
								}
							}
						}
					} else {
						ObjectFlow flowNew = Application.getInstance().getProject().getElementsFactory()
								.createObjectFlowInstance();
						acEdges.put(listEdges.get(e), flowNew);

						flowNew.setActivity(cActivity);
						flowNew.setGuard(flowOld.getGuard());
						flowNew.setInStructuredNode(flowOld.getInStructuredNode());
						flowNew.setInterrupts(flowOld.getInterrupts());
						flowNew.setMulticast(flowOld.isMulticast());
						flowNew.setMultireceive(flowOld.isMultireceive());
						flowNew.setName(flowOld.getName());
						flowNew.setNameExpression(flowOld.getNameExpression());
						flowNew.setSelection(flowOld.getSelection());
						flowNew.setSource(acNodes.get(flowOld.getSource()));
						flowNew.setTarget(acNodes.get(flowOld.getTarget()));
						flowNew.setTransformation(flowOld.getTransformation());
						flowNew.setVisibility(flowOld.getVisibility());
						flowNew.setWeight(flowOld.getWeight());
						flowNew.setOwner(cActivity);
					}

				} else if (listEdges.get(e) instanceof ControlFlow) {//khanh
					int iStop = 10;
					ControlFlow flow = ((ControlFlow) (listEdges.get(e)));
					ControlFlow flowStart = ((ControlFlow) (listEdges.get(e)));

					if ((flow.getTarget() instanceof Action) && ((StereotypesHelper.hasStereotype(flow.getTarget(),
							UWEStereotypeRequirementsActions.DISPLAY_ACTION.toString()))
							|| (StereotypesHelper.hasStereotype(flow.getTarget(),
									UWEStereotypeRequirementsActions.NAVIGATION_ACTION.toString())))) {
						ActivityNode source = flow.getSource();
						for (int e2 = 0; e2 < listEdges.size(); e2++) {
							if (e2 == e) {
								continue;
							}

							if ((listEdges.get(e2) instanceof ControlFlow)
									&& (listEdges.get(e2).getTarget() instanceof Action)
									&& ((StereotypesHelper.hasStereotype(listEdges.get(e2).getTarget(),
											UWEStereotypeRequirementsActions.DISPLAY_ACTION.toString()))
											|| (StereotypesHelper.hasStereotype(listEdges.get(e2).getTarget(),
													UWEStereotypeRequirementsActions.NAVIGATION_ACTION.toString())))) {
								if (listEdges.get(e2).getSource() == flow.getTarget()) {
									iStop--;
									if (iStop > 0) {
										flow = ((ControlFlow) (listEdges.get(e2)));
										e2 = 0;
									}
								}
							} else if (listEdges.get(e2) instanceof ControlFlow) {
								ControlFlow flowOld = ((ControlFlow) (listEdges.get(e2)));
								if (flowOld.getSource() == flow.getTarget()) {
									ControlFlow flowNew = Application.getInstance().getProject().getElementsFactory()
											.createControlFlowInstance();
									acEdges.put(flowStart, flowNew);

									flowNew.setActivity(cActivity);
									flowNew.setGuard(flow.getGuard());
									flowNew.setInStructuredNode(flow.getInStructuredNode());
									flowNew.setInterrupts(flow.getInterrupts());
									flowNew.setName(flow.getName());
									flowNew.setNameExpression(flow.getNameExpression());
									flowNew.setSource(acNodes.get(source));
									flowNew.setTarget(acNodes.get(flowOld.getTarget()));
									flowNew.setVisibility(flow.getVisibility());
									flowNew.setWeight(flow.getWeight());
									flowNew.setOwner(cActivity);
								}
							} else if (listEdges.get(e2) instanceof ObjectFlow) {
								ObjectFlow flowOld2 = ((ObjectFlow) (listEdges.get(e2)));
								if (flowOld2.getSource() == flow.getTarget()) {
									ObjectFlow flowNew = Application.getInstance().getProject().getElementsFactory()
											.createObjectFlowInstance();
									acEdges.put(listEdges.get(e), flowNew);

									flowNew.setActivity(cActivity);
									flowNew.setGuard(flow.getGuard());
									flowNew.setInStructuredNode(flow.getInStructuredNode());
									flowNew.setInterrupts(flow.getInterrupts());
									flowNew.setMulticast(flowOld2.isMulticast());
									flowNew.setMultireceive(flowOld2.isMultireceive());
									flowNew.setName(flow.getName());
									flowNew.setNameExpression(flow.getNameExpression());
									flowNew.setSelection(flowOld2.getSelection());
									flowNew.setSource(acNodes.get(flow.getSource()));
									flowNew.setTarget(acNodes.get(flowOld2.getTarget()));
									flowNew.setTransformation(flowOld2.getTransformation());
									flowNew.setVisibility(flow.getVisibility());
									flowNew.setWeight(flow.getWeight());
									flowNew.setOwner(cActivity);
								}
							}
						}
					} else {
						ControlFlow flowNew = Application.getInstance().getProject().getElementsFactory()
								.createControlFlowInstance();
						acEdges.put(listEdges.get(e), flowNew);

						flowNew.setActivity(cActivity);
						flowNew.setGuard(flow.getGuard());
						flowNew.setInStructuredNode(flow.getInStructuredNode());
						flowNew.setInterrupts(flow.getInterrupts());
						flowNew.setName(flow.getName());
						flowNew.setNameExpression(flow.getNameExpression());
						flowNew.setSource(acNodes.get(flow.getSource()));
						flowNew.setTarget(acNodes.get(flow.getTarget()));
						flowNew.setVisibility(flow.getVisibility());
						flowNew.setWeight(flow.getWeight());
						flowNew.setOwner(cActivity);
					}
				}
			}
			if ((bCreated) && (SessionManager.getInstance().isSessionCreated())) {
				SessionManager.getInstance().closeSession();
			}

			if (!SessionManager.getInstance().isSessionCreated()) {
				bCreated = true;
				SessionManager.getInstance().createSession("Create Workflow");
			}

			Iterator<Diagram> itDiagrams = ((Activity) (listActivities.get(i))).getOwnedDiagram().iterator();
			while (itDiagrams.hasNext()) {
				HashMap<Element, PresentationElement> acElementPresentation = new HashMap<Element, PresentationElement>();
				Diagram diagramOld = ((Diagram) (itDiagrams.next()));

				DiagramPresentationElement prOld = Application.getInstance().getProject().getDiagram(diagramOld);
				prOld.open();
				try {
					Diagram diagramNew = null;
					//if(dg == null) {
						diagramNew = ModelElementsManager.getInstance()
							.createDiagram(DiagramTypeConstants.UML_ACTIVITY_DIAGRAM, cActivity);
					         Application.getInstance().getGUILog().log("addWorkflow createDiagram(DiagramTypeConstants.UML_ACTIVITY_DIAGRAM ");
					//}else {
						// diagramNew = dg;
						// Application.getInstance().getGUILog().log("addWorkflow Using dg ");
					//}
					
					

					Iterator<PresentationElement> itPresentations = prOld.getPresentationElements().iterator();
					ArrayList<PresentationElement> tempPresentations = new ArrayList<PresentationElement>();
					ArrayList<PresentationElement> tempParents = new ArrayList<PresentationElement>();
					Application.getInstance().getGUILog().log("addWorkflow itPresentations ");
					while (itPresentations.hasNext()) {
						tempPresentations.add(itPresentations.next());
						tempParents.add(null);
					}
					
					Application.getInstance().getGUILog().log("addWorkflow tempPresentations ");

					for (int p = 0; p < tempPresentations.size(); p++) {
						ShapeElement elShape = null;
						Application.getInstance().getGUILog().log("addWorkflow tempPresentations  1111");
						if (tempPresentations.get(p).getElement() instanceof ActivityNode) {
							int iAddParent = 0;
							if (tempPresentations.get(p).getElement() instanceof StructuredActivityNode) {
								Iterator<PresentationElement> itPresentationsIn = tempPresentations.get(p)
										.getPresentationElements().iterator();
								while (itPresentationsIn.hasNext()) {
									PresentationElement tempPresentation = itPresentationsIn.next();

									boolean bAdd = true;
									for (int a = 0; a < tempPresentations.size(); a++) {
										if (tempPresentation.getElement() == tempPresentations.get(a).getElement()) {
											bAdd = false;
										}
									}

									if (bAdd) {
										tempPresentations.add(tempPresentation);
										iAddParent++;
									}
								}
							}
							Application.getInstance().getGUILog().log("addWorkflow tempPresentations  2222");
							if (acNodes.get(((ActivityNode) (tempPresentations.get(p).getElement()))) != null) {
								try {
									if (tempParents.get(p) == null) {

										elShape = PresentationElementsManager.getInstance().createShapeElement(
												acNodes.get(((ActivityNode) (tempPresentations.get(p).getElement()))),
												Application.getInstance().getProject().getDiagram(diagramNew));
									} else {
										elShape = PresentationElementsManager.getInstance().createShapeElement(
												acNodes.get(((ActivityNode) (tempPresentations.get(p).getElement()))),
												tempParents.get(p));
									}

									if (elShape != null) {
										for (int t = 0; t < iAddParent; t++) {
											tempParents.add(elShape);
										}
										Rectangle rectBounds = tempPresentations.get(p).getBounds();
										PresentationElementsManager.getInstance().reshapeShapeElement(elShape,
												rectBounds);
										acElementPresentation.put(
												acNodes.get(((ActivityNode) (tempPresentations.get(p).getElement()))),
												elShape);
										listShapes.add(elShape);
										listMasters.add(tempPresentations.get(p));
									}
								} catch (Exception e) {
									Application.getInstance().getGUILog().showError(
											"Exception 11111 in ReqToProTransformationRules.addWorkflow(): " + e.toString());
								}
							}
						}
					}

					itPresentations = prOld.getPresentationElements().iterator();
					while (itPresentations.hasNext()) {
						tempPresentations.add(itPresentations.next());
					}

					for (int p = 0; p < tempPresentations.size(); p++) {
						ShapeElement elShape = null;
						Application.getInstance().getGUILog().log("addWorkflow tempPresentations  3333");

						if (tempPresentations.get(p).getElement() instanceof ActivityNode) {
							if (tempPresentations.get(p).getElement() instanceof StructuredActivityNode) {
								Iterator<PresentationElement> itPresentationsIn = tempPresentations.get(p)
										.getPresentationElements().iterator();
								while (itPresentationsIn.hasNext()) {
									tempPresentations.add(itPresentationsIn.next());
								}
							}

							Iterator<PresentationElement> itPresentations2 = tempPresentations.get(p)
									.getPresentationElements().iterator();

							ArrayList<ShapeElement> shapePins = new ArrayList<ShapeElement>();
							while (itPresentations2.hasNext()) {
								PresentationElement tempPresentation = itPresentations2.next();

								if (tempPresentation.getElement() instanceof Pin) {
									try {
										for (int s = 0; s < listShapes.size(); s++) {
											if ((acNodes.get(((Pin) (tempPresentation.getElement()))) != null)
													&& (listShapes.get(s).getElement() == acNodes
															.get(((Pin) (tempPresentation.getElement()))).getOwner())) {
												elShape = listShapes.get(s);
												break;
											}
										}

										if (elShape != null) {
											ShapeElement elShape2 = PresentationElementsManager.getInstance()
													.createShapeElement(
															acNodes.get(((Pin) (tempPresentation.getElement()))),
															elShape);

											elShape2.setBounds(tempPresentation.getBounds());
											shapePins.add(elShape2);
											elShape.addPresentationElement(elShape2);
											acElementPresentation.put(
													acNodes.get(((Pin) (tempPresentation.getElement()))), elShape2);
											listShapes.add(elShape2);

											listMasters.add(tempPresentation);
										}
									} catch (Exception e) {
										Application.getInstance().getGUILog().showError(
												"Exception 22222 in ReqToProTransformation.addWorkflow(): " + e.toString());
									}
								}
							}
							shapeActionPins.add(shapePins);
						}
					}

					itPresentations = prOld.getPresentationElements().iterator();
					while (itPresentations.hasNext()) {
						Application.getInstance().getGUILog().log("addWorkflow tempPresentations  333333333333");
						try {
							PresentationElement tempPresentation = itPresentations.next();
							if ((tempPresentation.getElement() instanceof ActivityEdge)
									&& (acEdges.get(((ActivityEdge) (tempPresentation.getElement()))) != null)) {
								PathElement pathElement = PresentationElementsManager.getInstance().createPathElement(
										acEdges.get(((ActivityEdge) (tempPresentation.getElement()))),
										acElementPresentation.get(acEdges
												.get(((ActivityEdge) (tempPresentation.getElement()))).getSource()),
										acElementPresentation.get(acEdges
												.get(((ActivityEdge) (tempPresentation.getElement()))).getTarget()));

								if (pathElement != null) {
									Application.getInstance().getProject().getDiagram(diagramNew)
											.addPresentationElement(pathElement);
									Application.getInstance().getGUILog().log("######addWorkflow addPresentationElement(pathElement);33333");
									
								}
							}

							Iterator<PresentationElement> itPresentation2 = tempPresentation.getPresentationElements()
									.iterator();
							while (itPresentation2.hasNext()) {
								PresentationElement cTemp2 = itPresentation2.next();
								Application.getInstance().getGUILog().log("addWorkflow tempPresentations  555555555555555");

								if ((cTemp2.getElement() instanceof ActivityEdge)
										&& (acEdges.get(((ActivityEdge) (cTemp2.getElement()))) != null)) {
									PathElement cPath = PresentationElementsManager.getInstance().createPathElement(
											acEdges.get(((ActivityEdge) (cTemp2.getElement()))),
											acElementPresentation.get(
													acEdges.get(((ActivityEdge) (cTemp2.getElement()))).getSource()),
											acElementPresentation.get(
													acEdges.get(((ActivityEdge) (cTemp2.getElement()))).getTarget()));
									Application.getInstance().getGUILog().log("addWorkflow addPresentationElement(pathElement);5555");
									Application.getInstance().getProject().getDiagram(diagramNew)
											.addPresentationElement(cPath);
								}
							}
						} catch (Exception e) {
							Application.getInstance().getGUILog()
									.showError("Exception  33333in  ReqToProTransformation.addWorkflow(): " + e.toString());
						}
					}
					prOld.close();
				} catch (Exception e) {
					Application.getInstance().getGUILog()
							.showError("Exception 4444 in ReqToProTransformation.addWorkflow(): " + e.toString());
					return (false);
				}
			}

			if ((bCreated) && (SessionManager.getInstance().isSessionCreated())) {
				SessionManager.getInstance().closeSession();
			}

		}

		for (int a = 0; a < shapeActionPins.size(); a++) {
			Application.getInstance().getGUILog().log("######addWorkflow addPresentationElement(pathElement);666666666666666");
			int iTries = 10;
			boolean bRetry = false;
			for (int p = 0; p < shapeActionPins.get(a).size(); p++) {
				for (int p2 = 0; p2 < p; p2++) {
					if (shapeActionPins.get(a).get(p2).getBounds()
							.intersects(shapeActionPins.get(a).get(p).getBounds())) {
						Rectangle rectTemp = shapeActionPins.get(a).get(p2).getBounds();
						rectTemp.x += shapeActionPins.get(a).get(p).getBounds().width * 2;
						rectTemp.y += shapeActionPins.get(a).get(p).getBounds().height;
						shapeActionPins.get(a).get(p2).setBounds(rectTemp);

						bRetry = true;
					}
				}
				if ((bRetry) && (iTries > 0)) {
					p = -1;
					iTries--;
					bRetry = false;
				}
			}
		}

		for (int i = 0; i < listShapes.size(); i++) {
			Application.getInstance().getGUILog().log("######addWorkflow addPresentationElement(pathElement);77777777777777777");

			com.nomagic.magicdraw.properties.Property cProp = listShapes.get(i).getProperty("STEREOTYPES_DISPLAY_MODE")
					.clone();
			cProp.setValue("STEREOTYPE_DISPLAY_MODE_ICON");
			listShapes.get(i).changeProperty(cProp);

			if ((listShapes.get(i).getProperty("SHOW_NAME") != null)
					&& ((listMasters.get(i).getProperty("SHOW_NAME") == null)
							|| (listMasters.get(i).getProperty("SHOW_NAME").getValue().equals(false)))) {
				cProp = listShapes.get(i).getProperty("SHOW_NAME").clone();
				cProp.setValue(false);
				listShapes.get(i).changeProperty(cProp);
			}
		}

		for (int i = 0; i < nodesRemove.size(); i++) {
			nodesRemove.get(i).dispose();
		}
		return (true);
	}
}