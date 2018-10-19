package jports.data;

public abstract class Delete<Target> extends Filterable<Delete<Target>> {

	private Target target;

	public Delete(Target target) {
		this.target = target;
	}

	public Delete<Target> setTarget(Target value) {
		this.target = value;
		return this;
	}

	public Target getTarget() {
		return this.target;
	}

	@Override
	protected Delete<Target> getThis() {
		return this;
	}

	public abstract int execute();

}
