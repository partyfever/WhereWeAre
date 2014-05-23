package rest.adapters;

import java.util.List;

public interface BasicAdapter <X,Y> {
	X fromTransfer(Y transfer);
	Y toTransfer(X model);
	List<X> fromTransfer(List<Y> transfers);
	List<Y> toTransfer(List<X> models);
}
