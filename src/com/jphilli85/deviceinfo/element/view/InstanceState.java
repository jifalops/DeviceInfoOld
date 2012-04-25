package com.jphilli85.deviceinfo.element.view;

import android.os.Bundle;

public interface InstanceState {
	void saveState(Bundle outState);
	void restoreState(Bundle savedState);
}
