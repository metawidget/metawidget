# Metawidget

Metawidget ${project.version} is a smart User Interface widget that
populates itself with UI components to match the properties of your
domain objects. Can be used through Node.js for server-side UI generation.

## Introduction
For an introduction to Metawidget, please see the tutorial at
http://metawidget.org/doc/reference/en/html/ch01s02.html

The Node.js version of Metawidget must be used in combination with a DOM
implementation. This can either be jsdom, envjs, or even a simple implementation
of your own (see test/render.js inside this module for an example). Metawidget
must be wrapped around a DOM element. The Metawidget constructor takes this
element, and thereafter always uses `element.ownerDocument` rather than
referencing any global `document` object.

## Installation
`npm install metawidget`

## Usage
See `test/render.js` inside this module for a working example. But briefly:

    var metawidget = require( 'metawidget' );
    ...
    var mw = new metawidget.Metawidget( yourElement );
    mw.toInspect = {
	    firstname: "Joe",
	    surname: "Bloggs"
    };
    mw.buildWidgets();
    ...
    // yourElement is now populated with child components

## License

This software is dual licensed under both the FSF Lesser GNU Public License (LGPL)
and a commercial license (see http://metawidget.org).
