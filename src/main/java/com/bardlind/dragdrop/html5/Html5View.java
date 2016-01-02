package com.bardlind.dragdrop.html5;

import io.dropwizard.views.View;

/**
 * Created by baardl on 23.10.15.
 */
public class Html5View extends View {
    public static final String TEMPLATE_NAME = "html5.ftl";

    public Html5View() {
        super(TEMPLATE_NAME);
    }
}

