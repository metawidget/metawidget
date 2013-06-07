# metawidget

Metawidget ${project.version} is a smart User Interface widget that
populates itself, at runtime, with UI components to match the
properties of your business objects.

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
See test/render.js inside this module for a working example. But briefly:

`var metawidget = require( 'metawidget' );  
...  
var mw = new metawidget.Metawidget( yourElement );  
mw.toInspect = {  
	firstname: "Joe",  
	surname: "Bloggs"  
};  
mw.buildWidgets();  
...  
// Do something with yourElement`

## License

(The LGPL License)

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA