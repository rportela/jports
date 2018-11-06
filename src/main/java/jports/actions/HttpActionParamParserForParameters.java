package jports.actions;

import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;

import jports.adapters.AdapterAspect;
import jports.adapters.AdapterAspectMember;

public class HttpActionParamParserForParameters implements HttpActionParamParser {

	public <T> T parseParams(Class<T> paramsClass, HttpServletRequest request) throws Exception {

		if (Void.class.equals(paramsClass))
			return null;

		Constructor<T> constructor = paramsClass.getConstructor();
		T params = constructor.newInstance();
		AdapterAspect<T> aspect = AdapterAspect.getInstance(paramsClass);
		for (AdapterAspectMember<T> member : aspect) {
			String vtext = request.getParameter(member.getName());
			member.setValue(params, vtext);
		}
		return params;
	}

}
