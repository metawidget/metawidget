// -------------------------------------------------------------------
// Image Thumbnail Viewer Script- By Dynamic Drive, available at: http://www.dynamicdrive.com
// Last updated: July 7th, 2008- Fixed enlarged image not showing in IE sometimes
// -------------------------------------------------------------------

var thumbnailviewer={
enableTitle: true, //Should "title" attribute of link be used as description?
enableAnimation: true, //Enable fading animation?
definefooter: '<div class="footerbar">CLOSE X</div>', //Define HTML for footer interface
defineLoading: '<img src="/media/loading.gif" /> Loading Image...', //Define HTML for "loading" div

/////////////No need to edit beyond here/////////////////////////

scrollbarwidth: 16,
opacitystring: 'filter:progid:DXImageTransform.Microsoft.alpha(opacity=10); -moz-opacity: 0.1; opacity: 0.1',
targetlinks:[], //Array to hold links with rel="thumbnail"

createthumbBox:function(){
//write out HTML for Image Thumbnail Viewer plus loading div
document.write('<div id="thumbBox" onClick="thumbnailviewer.closeit()"><div id="thumbImage"></div>'+this.definefooter+'</div>')
document.write('<div id="thumbLoading">'+this.defineLoading+'</div>')
this.thumbBox=document.getElementById("thumbBox")
this.thumbImage=document.getElementById("thumbImage") //Reference div that holds the shown image
this.thumbLoading=document.getElementById("thumbLoading") //Reference "loading" div that will be shown while image is fetched
this.standardbody=(document.compatMode=="CSS1Compat")? document.documentElement : document.body //create reference to common "body" across doctypes
},


centerDiv:function(divobj){ //Centers a div element on the page
var ie=document.all && !window.opera
var dom=document.getElementById
var scroll_top=(ie)? this.standardbody.scrollTop : window.pageYOffset
var scroll_left=(ie)? this.standardbody.scrollLeft : window.pageXOffset
var docwidth=(ie)? this.standardbody.clientWidth : window.innerWidth-this.scrollbarwidth
var docheight=(ie)? this.standardbody.clientHeight: window.innerHeight
var docheightcomplete=(this.standardbody.offsetHeight>this.standardbody.scrollHeight)? this.standardbody.offsetHeight : this.standardbody.scrollHeight //Full scroll height of document
var objwidth=divobj.offsetWidth //width of div element
var objheight=divobj.offsetHeight //height of div element
var topposition=(docheight>objheight)? scroll_top+docheight/2-objheight/2+"px" : scroll_top+10+"px" //Vertical position of div element: Either centered, or if element height larger than viewpoint height, 10px from top of viewpoint
divobj.style.left=docwidth/2-objwidth/2+"px" //Center div element horizontally
divobj.style.top=Math.floor(parseInt(topposition))+"px"
divobj.style.visibility="visible"
},

showthumbBox:function(){ //Show ThumbBox div
thumbnailviewer.thumbLoading.style.visibility="hidden" //Hide "loading" div
this.centerDiv(this.thumbBox)
if (this.enableAnimation){ //If fading animation enabled
this.currentopacity=0.1 //Starting opacity value
this.opacitytimer=setInterval("thumbnailviewer.opacityanimation()", 20)
}
},


loadimage:function(link){ //Load image function that gets attached to each link on the page with rel="thumbnail"
if (this.thumbBox.style.visibility=="visible") //if thumbox is visible on the page already
this.closeit() //Hide it first (not doing so causes triggers some positioning bug in Firefox
var imageHTML='<img src="'+link.getAttribute("href")+'" style="'+this.opacitystring+'" />' //Construct HTML for shown image
if (this.enableTitle && link.getAttribute("title")) //Use title attr of the link as description?
imageHTML=link.getAttribute("title")+'<br />'+imageHTML
this.centerDiv(this.thumbLoading) //Center and display "loading" div while we set up the image to be shown
this.thumbImage.innerHTML=imageHTML //Populate thumbImage div with shown image's HTML (while still hidden)
this.featureImage=this.thumbImage.getElementsByTagName("img")[0] //Reference shown image itself
if (this.featureImage.complete)
thumbnailviewer.showthumbBox()
else{
this.featureImage.onload=function(){ //When target image has completely loaded
thumbnailviewer.showthumbBox() //Display "thumbbox" div to the world!
}
}
if (document.all && !window.createPopup) //Target IE5.0 browsers only. Address IE image cache not firing onload bug: panoramio.com/blog/onload-event/
this.featureImage.src=link.getAttribute("href")
this.featureImage.onerror=function(){ //If an error has occurred while loading the image to show
thumbnailviewer.thumbLoading.style.visibility="hidden" //Hide "loading" div, game over
}
},

setimgopacity:function(value){ //Sets the opacity of "thumbimage" div per the passed in value setting (0 to 1 and in between)
var targetobject=this.featureImage
if (targetobject.filters && targetobject.filters[0]){ //IE syntax
if (typeof targetobject.filters[0].opacity=="number") //IE6
targetobject.filters[0].opacity=value*100
else //IE 5.5
targetobject.style.filter="alpha(opacity="+value*100+")"
}
else if (typeof targetobject.style.MozOpacity!="undefined") //Old Mozilla syntax
targetobject.style.MozOpacity=value
else if (typeof targetobject.style.opacity!="undefined") //Standard opacity syntax
targetobject.style.opacity=value
else //Non of the above, stop opacity animation
this.stopanimation()
},

opacityanimation:function(){ //Gradually increase opacity function
this.setimgopacity(this.currentopacity)
this.currentopacity+=0.1
if (this.currentopacity>1)
this.stopanimation()
},

stopanimation:function(){
if (typeof this.opacitytimer!="undefined")
clearInterval(this.opacitytimer)
},


closeit:function(){ //Close "thumbbox" div function
this.stopanimation()
this.thumbBox.style.visibility="hidden"
this.thumbImage.innerHTML=""
this.thumbBox.style.left="-2000px"
this.thumbBox.style.top="-2000px"
},

cleanup:function(){ //Clean up routine on page unload
this.thumbLoading=null
if (this.featureImage) this.featureImage.onload=null
this.featureImage=null
this.thumbImage=null
for (var i=0; i<this.targetlinks.length; i++)
this.targetlinks[i].onclick=null
this.thumbBox=null
},

dotask:function(target, functionref, tasktype){ //assign a function to execute to an event handler (ie: onunload)
var tasktype=(window.addEventListener)? tasktype : "on"+tasktype
if (target.addEventListener)
target.addEventListener(tasktype, functionref, false)
else if (target.attachEvent)
target.attachEvent(tasktype, functionref)
},

init:function(){ //Initialize thumbnail viewer script by scanning page and attaching appropriate function to links with rel="thumbnail"
if (!this.enableAnimation)
this.opacitystring=""
var pagelinks=document.getElementsByTagName("a")
for (var i=0; i<pagelinks.length; i++){ //BEGIN FOR LOOP
if (pagelinks[i].getAttribute("rel") && pagelinks[i].getAttribute("rel")=="thumbnail"){ //Begin if statement
pagelinks[i].onclick=function(){
thumbnailviewer.stopanimation() //Stop any currently running fade animation on "thumbbox" div before proceeding
thumbnailviewer.loadimage(this) //Load image
return false
}
this.targetlinks[this.targetlinks.length]=pagelinks[i] //store reference to target link
} //end if statement
} //END FOR LOOP
//Reposition "thumbbox" div when page is resized
this.dotask(window, function(){if (thumbnailviewer.thumbBox.style.visibility=="visible") thumbnailviewer.centerDiv(thumbnailviewer.thumbBox)}, "resize")


} //END init() function

}

thumbnailviewer.createthumbBox() //Output HTML for the image thumbnail viewer
thumbnailviewer.dotask(window, function(){thumbnailviewer.init()}, "load") //Initialize script on page load
thumbnailviewer.dotask(window, function(){thumbnailviewer.cleanup()}, "unload")