package jports.xml.soap;

import jports.actions.Action;
import jports.actions.ActionExecution;

public class SoapAction<TParams extends SoapActionParameter, TResult extends SoapActionResponse>
		extends Action<TParams, TResult> {

	@Override
	protected void mainFlow(ActionExecution<TParams, TResult> execution) {

		TParams params = execution.params;
		
		
	}

}
