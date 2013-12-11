var Prototype={Version:"1.7",Browser:(function(){var B=navigator.userAgent;
var A=Object.prototype.toString.call(window.opera)=="[object Opera]";
return{IE:!!window.attachEvent&&!A,Opera:A,WebKit:B.indexOf("AppleWebKit/")>-1,Gecko:B.indexOf("Gecko")>-1&&B.indexOf("KHTML")===-1,MobileSafari:/Apple.*Mobile/.test(B)}
})(),BrowserFeatures:{XPath:!!document.evaluate,SelectorsAPI:!!document.querySelector,ElementExtensions:(function(){var A=window.Element||window.HTMLElement;
return !!(A&&A.prototype)
})(),SpecificElementExtensions:(function(){if(typeof window.HTMLDivElement!=="undefined"){return true
}var C=document.createElement("div"),B=document.createElement("form"),A=false;
if(C.__proto__&&(C.__proto__!==B.__proto__)){A=true
}C=B=null;
return A
})()},ScriptFragment:"<script[^>]*>([\\S\\s]*?)<\/script>",JSONFilter:/^\/\*-secure-([\s\S]*)\*\/\s*$/,emptyFunction:function(){},K:function(A){return A
}};
if(Prototype.Browser.MobileSafari){Prototype.BrowserFeatures.SpecificElementExtensions=false
}var Class=(function(){var D=(function(){for(var E in {toString:1}){if(E==="toString"){return false
}}return true
})();
function A(){}function B(){var H=null,G=$A(arguments);
if(Object.isFunction(G[0])){H=G.shift()
}function E(){this.initialize.apply(this,arguments)
}Object.extend(E,Class.Methods);
E.superclass=H;
E.subclasses=[];
if(H){A.prototype=H.prototype;
E.prototype=new A;
H.subclasses.push(E)
}for(var F=0,I=G.length;
F<I;
F++){E.addMethods(G[F])
}if(!E.prototype.initialize){E.prototype.initialize=Prototype.emptyFunction
}E.prototype.constructor=E;
return E
}function C(K){var G=this.superclass&&this.superclass.prototype,F=Object.keys(K);
if(D){if(K.toString!=Object.prototype.toString){F.push("toString")
}if(K.valueOf!=Object.prototype.valueOf){F.push("valueOf")
}}for(var E=0,H=F.length;
E<H;
E++){var J=F[E],I=K[J];
if(G&&Object.isFunction(I)&&I.argumentNames()[0]=="$super"){var L=I;
I=(function(M){return function(){return G[M].apply(this,arguments)
}
})(J).wrap(L);
I.valueOf=L.valueOf.bind(L);
I.toString=L.toString.bind(L)
}this.prototype[J]=I
}return this
}return{create:B,Methods:{addMethods:C}}
})();
(function(){var c=Object.prototype.toString,b="Null",O="Undefined",V="Boolean",F="Number",S="String",h="Object",T="[object Function]",Y="[object Boolean]",G="[object Number]",L="[object String]",H="[object Array]",X="[object Date]",I=window.JSON&&typeof JSON.stringify==="function"&&JSON.stringify(0)==="0"&&typeof JSON.stringify(Prototype.K)==="undefined";
function K(j){switch(j){case null:return b;
case (void 0):return O
}var i=typeof j;
switch(i){case"boolean":return V;
case"number":return F;
case"string":return S
}return h
}function Z(i,k){for(var j in k){i[j]=k[j]
}return i
}function g(i){try{if(C(i)){return"undefined"
}if(i===null){return"null"
}return i.inspect?i.inspect():String(i)
}catch(j){if(j instanceof RangeError){return"..."
}throw j
}}function d(i){return f("",{"":i},[])
}function f(s,p,q){var r=p[s],o=typeof r;
if(K(r)===h&&typeof r.toJSON==="function"){r=r.toJSON(s)
}var l=c.call(r);
switch(l){case G:case Y:case L:r=r.valueOf()
}switch(r){case null:return"null";
case true:return"true";
case false:return"false"
}o=typeof r;
switch(o){case"string":return r.inspect(true);
case"number":return isFinite(r)?String(r):"null";
case"object":for(var k=0,j=q.length;
k<j;
k++){if(q[k]===r){throw new TypeError()
}}q.push(r);
var n=[];
if(l===H){for(var k=0,j=r.length;
k<j;
k++){var m=f(k,r,q);
n.push(typeof m==="undefined"?"null":m)
}n="["+n.join(",")+"]"
}else{var t=Object.keys(r);
for(var k=0,j=t.length;
k<j;
k++){var s=t[k],m=f(s,r,q);
if(typeof m!=="undefined"){n.push(s.inspect(true)+":"+m)
}}n="{"+n.join(",")+"}"
}q.pop();
return n
}}function W(i){return JSON.stringify(i)
}function J(i){return $H(i).toQueryString()
}function P(i){return i&&i.toHTML?i.toHTML():String.interpret(i)
}function R(i){if(K(i)!==h){throw new TypeError()
}var j=[];
for(var k in i){if(i.hasOwnProperty(k)){j.push(k)
}}return j
}function D(i){var j=[];
for(var k in i){j.push(i[k])
}return j
}function a(i){return Z({},i)
}function U(i){return !!(i&&i.nodeType==1)
}function M(i){return c.call(i)===H
}var B=(typeof Array.isArray=="function")&&Array.isArray([])&&!Array.isArray({});
if(B){M=Array.isArray
}function E(i){return i instanceof Hash
}function A(i){return c.call(i)===T
}function N(i){return c.call(i)===L
}function Q(i){return c.call(i)===G
}function e(i){return c.call(i)===X
}function C(i){return typeof i==="undefined"
}Z(Object,{extend:Z,inspect:g,toJSON:I?W:d,toQueryString:J,toHTML:P,keys:Object.keys||R,values:D,clone:a,isElement:U,isArray:M,isHash:E,isFunction:A,isString:N,isNumber:Q,isDate:e,isUndefined:C})
})();
Object.extend(Function.prototype,(function(){var K=Array.prototype.slice;
function D(O,L){var N=O.length,M=L.length;
while(M--){O[N+M]=L[M]
}return O
}function I(M,L){M=K.call(M,0);
return D(M,L)
}function G(){var L=this.toString().match(/^[\s\(]*function[^(]*\(([^)]*)\)/)[1].replace(/\/\/.*?[\r\n]|\/\*(?:.|[\r\n])*?\*\//g,"").replace(/\s+/g,"").split(",");
return L.length==1&&!L[0]?[]:L
}function H(N){if(arguments.length<2&&Object.isUndefined(arguments[0])){return this
}var L=this,M=K.call(arguments,1);
return function(){var O=I(M,arguments);
return L.apply(N,O)
}
}function F(N){var L=this,M=K.call(arguments,1);
return function(P){var O=D([P||window.event],M);
return L.apply(N,O)
}
}function J(){if(!arguments.length){return this
}var L=this,M=K.call(arguments,0);
return function(){var N=I(M,arguments);
return L.apply(this,N)
}
}function E(N){var L=this,M=K.call(arguments,1);
N=N*1000;
return window.setTimeout(function(){return L.apply(L,M)
},N)
}function A(){var L=D([0.01],arguments);
return this.delay.apply(this,L)
}function C(M){var L=this;
return function(){var N=D([L.bind(this)],arguments);
return M.apply(this,N)
}
}function B(){if(this._methodized){return this._methodized
}var L=this;
return this._methodized=function(){var M=D([this],arguments);
return L.apply(null,M)
}
}return{argumentNames:G,bind:H,bindAsEventListener:F,curry:J,delay:E,defer:A,wrap:C,methodize:B}
})());
(function(C){function B(){return this.getUTCFullYear()+"-"+(this.getUTCMonth()+1).toPaddedString(2)+"-"+this.getUTCDate().toPaddedString(2)+"T"+this.getUTCHours().toPaddedString(2)+":"+this.getUTCMinutes().toPaddedString(2)+":"+this.getUTCSeconds().toPaddedString(2)+"Z"
}function A(){return this.toISOString()
}if(!C.toISOString){C.toISOString=B
}if(!C.toJSON){C.toJSON=A
}})(Date.prototype);
RegExp.prototype.match=RegExp.prototype.test;
RegExp.escape=function(A){return String(A).replace(/([.*+?^=!:${}()|[\]\/\\])/g,"\\$1")
};
var PeriodicalExecuter=Class.create({initialize:function(B,A){this.callback=B;
this.frequency=A;
this.currentlyExecuting=false;
this.registerCallback()
},registerCallback:function(){this.timer=setInterval(this.onTimerEvent.bind(this),this.frequency*1000)
},execute:function(){this.callback(this)
},stop:function(){if(!this.timer){return 
}clearInterval(this.timer);
this.timer=null
},onTimerEvent:function(){if(!this.currentlyExecuting){try{this.currentlyExecuting=true;
this.execute();
this.currentlyExecuting=false
}catch(A){this.currentlyExecuting=false;
throw A
}}}});
Object.extend(String,{interpret:function(A){return A==null?"":String(A)
},specialChar:{"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r","\\":"\\\\"}});
Object.extend(String.prototype,(function(){var NATIVE_JSON_PARSE_SUPPORT=window.JSON&&typeof JSON.parse==="function"&&JSON.parse('{"test": true}').test;
function prepareReplacement(replacement){if(Object.isFunction(replacement)){return replacement
}var template=new Template(replacement);
return function(match){return template.evaluate(match)
}
}function gsub(pattern,replacement){var result="",source=this,match;
replacement=prepareReplacement(replacement);
if(Object.isString(pattern)){pattern=RegExp.escape(pattern)
}if(!(pattern.length||pattern.source)){replacement=replacement("");
return replacement+source.split("").join(replacement)+replacement
}while(source.length>0){if(match=source.match(pattern)){result+=source.slice(0,match.index);
result+=String.interpret(replacement(match));
source=source.slice(match.index+match[0].length)
}else{result+=source,source=""
}}return result
}function sub(pattern,replacement,count){replacement=prepareReplacement(replacement);
count=Object.isUndefined(count)?1:count;
return this.gsub(pattern,function(match){if(--count<0){return match[0]
}return replacement(match)
})
}function scan(pattern,iterator){this.gsub(pattern,iterator);
return String(this)
}function truncate(length,truncation){length=length||30;
truncation=Object.isUndefined(truncation)?"...":truncation;
return this.length>length?this.slice(0,length-truncation.length)+truncation:String(this)
}function strip(){return this.replace(/^\s+/,"").replace(/\s+$/,"")
}function stripTags(){return this.replace(/<\w+(\s+("[^"]*"|'[^']*'|[^>])+)?>|<\/\w+>/gi,"")
}function stripScripts(){return this.replace(new RegExp(Prototype.ScriptFragment,"img"),"")
}function extractScripts(){var matchAll=new RegExp(Prototype.ScriptFragment,"img"),matchOne=new RegExp(Prototype.ScriptFragment,"im");
return(this.match(matchAll)||[]).map(function(scriptTag){return(scriptTag.match(matchOne)||["",""])[1]
})
}function evalScripts(){return this.extractScripts().map(function(script){return eval(script)
})
}function escapeHTML(){return this.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;")
}function unescapeHTML(){return this.stripTags().replace(/&lt;/g,"<").replace(/&gt;/g,">").replace(/&amp;/g,"&")
}function toQueryParams(separator){var match=this.strip().match(/([^?#]*)(#.*)?$/);
if(!match){return{}
}return match[1].split(separator||"&").inject({},function(hash,pair){if((pair=pair.split("="))[0]){var key=decodeURIComponent(pair.shift()),value=pair.length>1?pair.join("="):pair[0];
if(value!=undefined){value=decodeURIComponent(value)
}if(key in hash){if(!Object.isArray(hash[key])){hash[key]=[hash[key]]
}hash[key].push(value)
}else{hash[key]=value
}}return hash
})
}function toArray(){return this.split("")
}function succ(){return this.slice(0,this.length-1)+String.fromCharCode(this.charCodeAt(this.length-1)+1)
}function times(count){return count<1?"":new Array(count+1).join(this)
}function camelize(){return this.replace(/-+(.)?/g,function(match,chr){return chr?chr.toUpperCase():""
})
}function capitalize(){return this.charAt(0).toUpperCase()+this.substring(1).toLowerCase()
}function underscore(){return this.replace(/::/g,"/").replace(/([A-Z]+)([A-Z][a-z])/g,"$1_$2").replace(/([a-z\d])([A-Z])/g,"$1_$2").replace(/-/g,"_").toLowerCase()
}function dasherize(){return this.replace(/_/g,"-")
}function inspect(useDoubleQuotes){var escapedString=this.replace(/[\x00-\x1f\\]/g,function(character){if(character in String.specialChar){return String.specialChar[character]
}return"\\u00"+character.charCodeAt().toPaddedString(2,16)
});
if(useDoubleQuotes){return'"'+escapedString.replace(/"/g,'\\"')+'"'
}return"'"+escapedString.replace(/'/g,"\\'")+"'"
}function unfilterJSON(filter){return this.replace(filter||Prototype.JSONFilter,"$1")
}function isJSON(){var str=this;
if(str.blank()){return false
}str=str.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@");
str=str.replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,"]");
str=str.replace(/(?:^|:|,)(?:\s*\[)+/g,"");
return(/^[\],:{}\s]*$/).test(str)
}function evalJSON(sanitize){var json=this.unfilterJSON(),cx=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;
if(cx.test(json)){json=json.replace(cx,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)
})
}try{if(!sanitize||json.isJSON()){return eval("("+json+")")
}}catch(e){}throw new SyntaxError("Badly formed JSON string: "+this.inspect())
}function parseJSON(){var json=this.unfilterJSON();
return JSON.parse(json)
}function include(pattern){return this.indexOf(pattern)>-1
}function startsWith(pattern){return this.lastIndexOf(pattern,0)===0
}function endsWith(pattern){var d=this.length-pattern.length;
return d>=0&&this.indexOf(pattern,d)===d
}function empty(){return this==""
}function blank(){return/^\s*$/.test(this)
}function interpolate(object,pattern){return new Template(this,pattern).evaluate(object)
}return{gsub:gsub,sub:sub,scan:scan,truncate:truncate,strip:String.prototype.trim||strip,stripTags:stripTags,stripScripts:stripScripts,extractScripts:extractScripts,evalScripts:evalScripts,escapeHTML:escapeHTML,unescapeHTML:unescapeHTML,toQueryParams:toQueryParams,parseQuery:toQueryParams,toArray:toArray,succ:succ,times:times,camelize:camelize,capitalize:capitalize,underscore:underscore,dasherize:dasherize,inspect:inspect,unfilterJSON:unfilterJSON,isJSON:isJSON,evalJSON:NATIVE_JSON_PARSE_SUPPORT?parseJSON:evalJSON,include:include,startsWith:startsWith,endsWith:endsWith,empty:empty,blank:blank,interpolate:interpolate}
})());
var Template=Class.create({initialize:function(A,B){this.template=A.toString();
this.pattern=B||Template.Pattern
},evaluate:function(A){if(A&&Object.isFunction(A.toTemplateReplacements)){A=A.toTemplateReplacements()
}return this.template.gsub(this.pattern,function(D){if(A==null){return(D[1]+"")
}var F=D[1]||"";
if(F=="\\"){return D[2]
}var B=A,G=D[3],E=/^([^.[]+|\[((?:.*?[^\\])?)\])(\.|\[|$)/;
D=E.exec(G);
if(D==null){return F
}while(D!=null){var C=D[1].startsWith("[")?D[2].replace(/\\\\]/g,"]"):D[1];
B=B[C];
if(null==B||""==D[3]){break
}G=G.substring("["==D[3]?D[1].length:D[0].length);
D=E.exec(G)
}return F+String.interpret(B)
})
}});
Template.Pattern=/(^|.|\r|\n)(#\{(.*?)\})/;
var $break={};
var Enumerable=(function(){function C(Y,X){var W=0;
try{this._each(function(a){Y.call(X,a,W++)
})
}catch(Z){if(Z!=$break){throw Z
}}return this
}function R(Z,Y,X){var W=-Z,a=[],b=this.toArray();
if(Z<1){return b
}while((W+=Z)<b.length){a.push(b.slice(W,W+Z))
}return a.collect(Y,X)
}function B(Y,X){Y=Y||Prototype.K;
var W=true;
this.each(function(a,Z){W=W&&!!Y.call(X,a,Z);
if(!W){throw $break
}});
return W
}function I(Y,X){Y=Y||Prototype.K;
var W=false;
this.each(function(a,Z){if(W=!!Y.call(X,a,Z)){throw $break
}});
return W
}function J(Y,X){Y=Y||Prototype.K;
var W=[];
this.each(function(a,Z){W.push(Y.call(X,a,Z))
});
return W
}function T(Y,X){var W;
this.each(function(a,Z){if(Y.call(X,a,Z)){W=a;
throw $break
}});
return W
}function H(Y,X){var W=[];
this.each(function(a,Z){if(Y.call(X,a,Z)){W.push(a)
}});
return W
}function G(Z,Y,X){Y=Y||Prototype.K;
var W=[];
if(Object.isString(Z)){Z=new RegExp(RegExp.escape(Z))
}this.each(function(b,a){if(Z.match(b)){W.push(Y.call(X,b,a))
}});
return W
}function A(W){if(Object.isFunction(this.indexOf)){if(this.indexOf(W)!=-1){return true
}}var X=false;
this.each(function(Y){if(Y==W){X=true;
throw $break
}});
return X
}function Q(X,W){W=Object.isUndefined(W)?null:W;
return this.eachSlice(X,function(Y){while(Y.length<X){Y.push(W)
}return Y
})
}function L(W,Y,X){this.each(function(a,Z){W=Y.call(X,W,a,Z)
});
return W
}function V(X){var W=$A(arguments).slice(1);
return this.map(function(Y){return Y[X].apply(Y,W)
})
}function P(Y,X){Y=Y||Prototype.K;
var W;
this.each(function(a,Z){a=Y.call(X,a,Z);
if(W==null||a>=W){W=a
}});
return W
}function N(Y,X){Y=Y||Prototype.K;
var W;
this.each(function(a,Z){a=Y.call(X,a,Z);
if(W==null||a<W){W=a
}});
return W
}function E(Z,X){Z=Z||Prototype.K;
var Y=[],W=[];
this.each(function(b,a){(Z.call(X,b,a)?Y:W).push(b)
});
return[Y,W]
}function F(X){var W=[];
this.each(function(Y){W.push(Y[X])
});
return W
}function D(Y,X){var W=[];
this.each(function(a,Z){if(!Y.call(X,a,Z)){W.push(a)
}});
return W
}function M(X,W){return this.map(function(Z,Y){return{value:Z,criteria:X.call(W,Z,Y)}
}).sort(function(d,c){var Z=d.criteria,Y=c.criteria;
return Z<Y?-1:Z>Y?1:0
}).pluck("value")
}function O(){return this.map()
}function S(){var X=Prototype.K,W=$A(arguments);
if(Object.isFunction(W.last())){X=W.pop()
}var Y=[this].concat(W).map($A);
return this.map(function(a,Z){return X(Y.pluck(Z))
})
}function K(){return this.toArray().length
}function U(){return"#<Enumerable:"+this.toArray().inspect()+">"
}return{each:C,eachSlice:R,all:B,every:B,any:I,some:I,collect:J,map:J,detect:T,findAll:H,select:H,filter:H,grep:G,include:A,member:A,inGroupsOf:Q,inject:L,invoke:V,max:P,min:N,partition:E,pluck:F,reject:D,sortBy:M,toArray:O,entries:O,zip:S,size:K,inspect:U,find:T}
})();
function $A(C){if(!C){return[]
}if("toArray" in Object(C)){return C.toArray()
}var B=C.length||0,A=new Array(B);
while(B--){A[B]=C[B]
}return A
}function $w(A){if(!Object.isString(A)){return[]
}A=A.strip();
return A?A.split(/\s+/):[]
}Array.from=$A;
(function(){var R=Array.prototype,M=R.slice,O=R.forEach;
function B(W,V){for(var U=0,X=this.length>>>0;
U<X;
U++){if(U in this){W.call(V,this[U],U,this)
}}}if(!O){O=B
}function L(){this.length=0;
return this
}function D(){return this[0]
}function G(){return this[this.length-1]
}function I(){return this.select(function(U){return U!=null
})
}function T(){return this.inject([],function(V,U){if(Object.isArray(U)){return V.concat(U.flatten())
}V.push(U);
return V
})
}function H(){var U=M.call(arguments,0);
return this.select(function(V){return !U.include(V)
})
}function F(U){return(U===false?this.toArray():this)._reverse()
}function K(U){return this.inject([],function(X,W,V){if(0==V||(U?X.last()!=W:!X.include(W))){X.push(W)
}return X
})
}function P(U){return this.uniq().findAll(function(V){return U.detect(function(W){return V===W
})
})
}function Q(){return M.call(this,0)
}function J(){return this.length
}function S(){return"["+this.map(Object.inspect).join(", ")+"]"
}function A(W,U){U||(U=0);
var V=this.length;
if(U<0){U=V+U
}for(;
U<V;
U++){if(this[U]===W){return U
}}return -1
}function N(V,U){U=isNaN(U)?this.length:(U<0?this.length+U:U)+1;
var W=this.slice(0,U).reverse().indexOf(V);
return(W<0)?W:U-W-1
}function C(){var Z=M.call(this,0),X;
for(var V=0,W=arguments.length;
V<W;
V++){X=arguments[V];
if(Object.isArray(X)&&!("callee" in X)){for(var U=0,Y=X.length;
U<Y;
U++){Z.push(X[U])
}}else{Z.push(X)
}}return Z
}Object.extend(R,Enumerable);
if(!R._reverse){R._reverse=R.reverse
}Object.extend(R,{_each:O,clear:L,first:D,last:G,compact:I,flatten:T,without:H,reverse:F,uniq:K,intersect:P,clone:Q,toArray:Q,size:J,inspect:S});
var E=(function(){return[].concat(arguments)[0][0]!==1
})(1,2);
if(E){R.concat=C
}if(!R.indexOf){R.indexOf=A
}if(!R.lastIndexOf){R.lastIndexOf=N
}})();
function $H(A){return new Hash(A)
}var Hash=Class.create(Enumerable,(function(){function E(P){this._object=Object.isHash(P)?P.toObject():Object.clone(P)
}function F(Q){for(var P in this._object){var R=this._object[P],S=[P,R];
S.key=P;
S.value=R;
Q(S)
}}function J(P,Q){return this._object[P]=Q
}function C(P){if(this._object[P]!==Object.prototype[P]){return this._object[P]
}}function M(P){var Q=this._object[P];
delete this._object[P];
return Q
}function O(){return Object.clone(this._object)
}function N(){return this.pluck("key")
}function L(){return this.pluck("value")
}function G(Q){var P=this.detect(function(R){return R.value===Q
});
return P&&P.key
}function I(P){return this.clone().update(P)
}function D(P){return new Hash(P).inject(this,function(Q,R){Q.set(R.key,R.value);
return Q
})
}function B(P,Q){if(Object.isUndefined(Q)){return P
}return P+"="+encodeURIComponent(String.interpret(Q))
}function A(){return this.inject([],function(T,W){var S=encodeURIComponent(W.key),Q=W.value;
if(Q&&typeof Q=="object"){if(Object.isArray(Q)){var V=[];
for(var R=0,P=Q.length,U;
R<P;
R++){U=Q[R];
V.push(B(S,U))
}return T.concat(V)
}}else{T.push(B(S,Q))
}return T
}).join("&")
}function K(){return"#<Hash:{"+this.map(function(P){return P.map(Object.inspect).join(": ")
}).join(", ")+"}>"
}function H(){return new Hash(this)
}return{initialize:E,_each:F,set:J,get:C,unset:M,toObject:O,toTemplateReplacements:O,keys:N,values:L,index:G,merge:I,update:D,toQueryString:A,inspect:K,toJSON:O,clone:H}
})());
Hash.from=$H;
Object.extend(Number.prototype,(function(){function D(){return this.toPaddedString(2,16)
}function B(){return this+1
}function H(J,I){$R(0,this,true).each(J,I);
return this
}function G(K,J){var I=this.toString(J||10);
return"0".times(K-I.length)+I
}function A(){return Math.abs(this)
}function C(){return Math.round(this)
}function E(){return Math.ceil(this)
}function F(){return Math.floor(this)
}return{toColorPart:D,succ:B,times:H,toPaddedString:G,abs:A,round:C,ceil:E,floor:F}
})());
function $R(C,A,B){return new ObjectRange(C,A,B)
}var ObjectRange=Class.create(Enumerable,(function(){function B(F,D,E){this.start=F;
this.end=D;
this.exclusive=E
}function C(D){var E=this.start;
while(this.include(E)){D(E);
E=E.succ()
}}function A(D){if(D<this.start){return false
}if(this.exclusive){return D<this.end
}return D<=this.end
}return{initialize:B,_each:C,include:A}
})());
var Abstract={};
var Try={these:function(){var C;
for(var B=0,D=arguments.length;
B<D;
B++){var A=arguments[B];
try{C=A();
break
}catch(E){}}return C
}};
var Ajax={getTransport:function(){return Try.these(function(){return new XMLHttpRequest()
},function(){return new ActiveXObject("Msxml2.XMLHTTP")
},function(){return new ActiveXObject("Microsoft.XMLHTTP")
})||false
},activeRequestCount:0};
Ajax.Responders={responders:[],_each:function(A){this.responders._each(A)
},register:function(A){if(!this.include(A)){this.responders.push(A)
}},unregister:function(A){this.responders=this.responders.without(A)
},dispatch:function(D,B,C,A){this.each(function(E){if(Object.isFunction(E[D])){try{E[D].apply(E,[B,C,A])
}catch(F){}}})
}};
Object.extend(Ajax.Responders,Enumerable);
Ajax.Responders.register({onCreate:function(){Ajax.activeRequestCount++
},onComplete:function(){Ajax.activeRequestCount--
}});
Ajax.Base=Class.create({initialize:function(A){this.options={method:"post",asynchronous:true,contentType:"application/x-www-form-urlencoded",encoding:"UTF-8",parameters:"",evalJSON:true,evalJS:true};
Object.extend(this.options,A||{});
this.options.method=this.options.method.toLowerCase();
if(Object.isHash(this.options.parameters)){this.options.parameters=this.options.parameters.toObject()
}}});
Ajax.Request=Class.create(Ajax.Base,{_complete:false,initialize:function($super,B,A){$super(A);
this.transport=Ajax.getTransport();
this.request(B)
},request:function(B){this.url=B;
this.method=this.options.method;
var D=Object.isString(this.options.parameters)?this.options.parameters:Object.toQueryString(this.options.parameters);
if(!["get","post"].include(this.method)){D+=(D?"&":"")+"_method="+this.method;
this.method="post"
}if(D&&this.method==="get"){this.url+=(this.url.include("?")?"&":"?")+D
}this.parameters=D.toQueryParams();
try{var A=new Ajax.Response(this);
if(this.options.onCreate){this.options.onCreate(A)
}Ajax.Responders.dispatch("onCreate",this,A);
this.transport.open(this.method.toUpperCase(),this.url,this.options.asynchronous);
if(this.options.asynchronous){this.respondToReadyState.bind(this).defer(1)
}this.transport.onreadystatechange=this.onStateChange.bind(this);
this.setRequestHeaders();
this.body=this.method=="post"?(this.options.postBody||D):null;
this.transport.send(this.body);
if(!this.options.asynchronous&&this.transport.overrideMimeType){this.onStateChange()
}}catch(C){this.dispatchException(C)
}},onStateChange:function(){var A=this.transport.readyState;
if(A>1&&!((A==4)&&this._complete)){this.respondToReadyState(this.transport.readyState)
}},setRequestHeaders:function(){var E={"X-Requested-With":"XMLHttpRequest","X-Prototype-Version":Prototype.Version,Accept:"text/javascript, text/html, application/xml, text/xml, */*"};
if(this.method=="post"){E["Content-type"]=this.options.contentType+(this.options.encoding?"; charset="+this.options.encoding:"");
if(this.transport.overrideMimeType&&(navigator.userAgent.match(/Gecko\/(\d{4})/)||[0,2005])[1]<2005){E.Connection="close"
}}if(typeof this.options.requestHeaders=="object"){var C=this.options.requestHeaders;
if(Object.isFunction(C.push)){for(var B=0,D=C.length;
B<D;
B+=2){E[C[B]]=C[B+1]
}}else{$H(C).each(function(F){E[F.key]=F.value
})
}}for(var A in E){this.transport.setRequestHeader(A,E[A])
}},success:function(){var A=this.getStatus();
return !A||(A>=200&&A<300)||A==304
},getStatus:function(){try{if(this.transport.status===1223){return 204
}return this.transport.status||0
}catch(A){return 0
}},respondToReadyState:function(A){var C=Ajax.Request.Events[A],B=new Ajax.Response(this);
if(C=="Complete"){try{this._complete=true;
(this.options["on"+B.status]||this.options["on"+(this.success()?"Success":"Failure")]||Prototype.emptyFunction)(B,B.headerJSON)
}catch(D){this.dispatchException(D)
}var E=B.getHeader("Content-type");
if(this.options.evalJS=="force"||(this.options.evalJS&&this.isSameOrigin()&&E&&E.match(/^\s*(text|application)\/(x-)?(java|ecma)script(;.*)?\s*$/i))){this.evalResponse()
}}try{(this.options["on"+C]||Prototype.emptyFunction)(B,B.headerJSON);
Ajax.Responders.dispatch("on"+C,this,B,B.headerJSON)
}catch(D){this.dispatchException(D)
}if(C=="Complete"){this.transport.onreadystatechange=Prototype.emptyFunction
}},isSameOrigin:function(){var A=this.url.match(/^\s*https?:\/\/[^\/]*/);
return !A||(A[0]=="#{protocol}//#{domain}#{port}".interpolate({protocol:location.protocol,domain:document.domain,port:location.port?":"+location.port:""}))
},getHeader:function(A){try{return this.transport.getResponseHeader(A)||null
}catch(B){return null
}},evalResponse:function(){try{return eval((this.transport.responseText||"").unfilterJSON())
}catch(e){this.dispatchException(e)
}},dispatchException:function(A){(this.options.onException||Prototype.emptyFunction)(this,A);
Ajax.Responders.dispatch("onException",this,A)
}});
Ajax.Request.Events=["Uninitialized","Loading","Loaded","Interactive","Complete"];
Ajax.Response=Class.create({initialize:function(C){this.request=C;
var D=this.transport=C.transport,A=this.readyState=D.readyState;
if((A>2&&!Prototype.Browser.IE)||A==4){this.status=this.getStatus();
this.statusText=this.getStatusText();
this.responseText=String.interpret(D.responseText);
this.headerJSON=this._getHeaderJSON()
}if(A==4){var B=D.responseXML;
this.responseXML=Object.isUndefined(B)?null:B;
this.responseJSON=this._getResponseJSON()
}},status:0,statusText:"",getStatus:Ajax.Request.prototype.getStatus,getStatusText:function(){try{return this.transport.statusText||""
}catch(A){return""
}},getHeader:Ajax.Request.prototype.getHeader,getAllHeaders:function(){try{return this.getAllResponseHeaders()
}catch(A){return null
}},getResponseHeader:function(A){return this.transport.getResponseHeader(A)
},getAllResponseHeaders:function(){return this.transport.getAllResponseHeaders()
},_getHeaderJSON:function(){var A=this.getHeader("X-JSON");
if(!A){return null
}A=decodeURIComponent(escape(A));
try{return A.evalJSON(this.request.options.sanitizeJSON||!this.request.isSameOrigin())
}catch(B){this.request.dispatchException(B)
}},_getResponseJSON:function(){var A=this.request.options;
if(!A.evalJSON||(A.evalJSON!="force"&&!(this.getHeader("Content-type")||"").include("application/json"))||this.responseText.blank()){return null
}try{return this.responseText.evalJSON(A.sanitizeJSON||!this.request.isSameOrigin())
}catch(B){this.request.dispatchException(B)
}}});
Ajax.Updater=Class.create(Ajax.Request,{initialize:function($super,A,C,B){this.container={success:(A.success||A),failure:(A.failure||(A.success?null:A))};
B=Object.clone(B);
var D=B.onComplete;
B.onComplete=(function(E,F){this.updateContent(E.responseText);
if(Object.isFunction(D)){D(E,F)
}}).bind(this);
$super(C,B)
},updateContent:function(D){var C=this.container[this.success()?"success":"failure"],A=this.options;
if(!A.evalScripts){D=D.stripScripts()
}if(C=$(C)){if(A.insertion){if(Object.isString(A.insertion)){var B={};
B[A.insertion]=D;
C.insert(B)
}else{A.insertion(C,D)
}}else{C.update(D)
}}}});
Ajax.PeriodicalUpdater=Class.create(Ajax.Base,{initialize:function($super,A,C,B){$super(B);
this.onComplete=this.options.onComplete;
this.frequency=(this.options.frequency||2);
this.decay=(this.options.decay||1);
this.updater={};
this.container=A;
this.url=C;
this.start()
},start:function(){this.options.onComplete=this.updateComplete.bind(this);
this.onTimerEvent()
},stop:function(){this.updater.options.onComplete=undefined;
clearTimeout(this.timer);
(this.onComplete||Prototype.emptyFunction).apply(this,arguments)
},updateComplete:function(A){if(this.options.decay){this.decay=(A.responseText==this.lastText?this.decay*this.options.decay:1);
this.lastText=A.responseText
}this.timer=this.onTimerEvent.bind(this).delay(this.decay*this.frequency)
},onTimerEvent:function(){this.updater=new Ajax.Updater(this.container,this.url,this.options)
}});
function $(B){if(arguments.length>1){for(var A=0,D=[],C=arguments.length;
A<C;
A++){D.push($(arguments[A]))
}return D
}if(Object.isString(B)){B=document.getElementById(B)
}return Element.extend(B)
}if(Prototype.BrowserFeatures.XPath){document._getElementsByXPath=function(F,A){var C=[];
var E=document.evaluate(F,$(A)||document,null,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
for(var B=0,D=E.snapshotLength;
B<D;
B++){C.push(Element.extend(E.snapshotItem(B)))
}return C
}
}if(!Node){var Node={}
}if(!Node.ELEMENT_NODE){Object.extend(Node,{ELEMENT_NODE:1,ATTRIBUTE_NODE:2,TEXT_NODE:3,CDATA_SECTION_NODE:4,ENTITY_REFERENCE_NODE:5,ENTITY_NODE:6,PROCESSING_INSTRUCTION_NODE:7,COMMENT_NODE:8,DOCUMENT_NODE:9,DOCUMENT_TYPE_NODE:10,DOCUMENT_FRAGMENT_NODE:11,NOTATION_NODE:12})
}(function(C){function D(F,E){if(F==="select"){return false
}if("type" in E){return false
}return true
}var B=(function(){try{var E=document.createElement('<input name="x">');
return E.tagName.toLowerCase()==="input"&&E.name==="x"
}catch(F){return false
}})();
var A=C.Element;
C.Element=function(G,F){F=F||{};
G=G.toLowerCase();
var E=Element.cache;
if(B&&F.name){G="<"+G+' name="'+F.name+'">';
delete F.name;
return Element.writeAttribute(document.createElement(G),F)
}if(!E[G]){E[G]=Element.extend(document.createElement(G))
}var H=D(G,F)?E[G].cloneNode(false):document.createElement(G);
return Element.writeAttribute(H,F)
};
Object.extend(C.Element,A||{});
if(A){C.Element.prototype=A.prototype
}})(this);
Element.idCounter=1;
Element.cache={};
Element._purgeElement=function(B){var A=B._prototypeUID;
if(A){Element.stopObserving(B);
B._prototypeUID=void 0;
delete Element.Storage[A]
}};
Element.Methods={visible:function(A){return $(A).style.display!="none"
},toggle:function(A){A=$(A);
Element[Element.visible(A)?"hide":"show"](A);
return A
},hide:function(A){A=$(A);
A.style.display="none";
return A
},show:function(A){A=$(A);
A.style.display="";
return A
},remove:function(A){A=$(A);
A.parentNode.removeChild(A);
return A
},update:(function(){var D=(function(){var G=document.createElement("select"),H=true;
G.innerHTML='<option value="test">test</option>';
if(G.options&&G.options[0]){H=G.options[0].nodeName.toUpperCase()!=="OPTION"
}G=null;
return H
})();
var B=(function(){try{var G=document.createElement("table");
if(G&&G.tBodies){G.innerHTML="<tbody><tr><td>test</td></tr></tbody>";
var I=typeof G.tBodies[0]=="undefined";
G=null;
return I
}}catch(H){return true
}})();
var A=(function(){try{var G=document.createElement("div");
G.innerHTML="<link>";
var I=(G.childNodes.length===0);
G=null;
return I
}catch(H){return true
}})();
var C=D||B||A;
var F=(function(){var G=document.createElement("script"),I=false;
try{G.appendChild(document.createTextNode(""));
I=!G.firstChild||G.firstChild&&G.firstChild.nodeType!==3
}catch(H){I=true
}G=null;
return I
})();
function E(K,L){K=$(K);
var G=Element._purgeElement;
var M=K.getElementsByTagName("*"),J=M.length;
while(J--){G(M[J])
}if(L&&L.toElement){L=L.toElement()
}if(Object.isElement(L)){return K.update().insert(L)
}L=Object.toHTML(L);
var I=K.tagName.toUpperCase();
if(I==="SCRIPT"&&F){K.text=L;
return K
}if(C){if(I in Element._insertionTranslations.tags){while(K.firstChild){K.removeChild(K.firstChild)
}Element._getContentFromAnonymousElement(I,L.stripScripts()).each(function(N){K.appendChild(N)
})
}else{if(A&&Object.isString(L)&&L.indexOf("<link")>-1){while(K.firstChild){K.removeChild(K.firstChild)
}var H=Element._getContentFromAnonymousElement(I,L.stripScripts(),true);
H.each(function(N){K.appendChild(N)
})
}else{K.innerHTML=L.stripScripts()
}}}else{K.innerHTML=L.stripScripts()
}L.evalScripts.bind(L).defer();
return K
}return E
})(),replace:function(B,C){B=$(B);
if(C&&C.toElement){C=C.toElement()
}else{if(!Object.isElement(C)){C=Object.toHTML(C);
var A=B.ownerDocument.createRange();
A.selectNode(B);
C.evalScripts.bind(C).defer();
C=A.createContextualFragment(C.stripScripts())
}}B.parentNode.replaceChild(C,B);
return B
},insert:function(C,E){C=$(C);
if(Object.isString(E)||Object.isNumber(E)||Object.isElement(E)||(E&&(E.toElement||E.toHTML))){E={bottom:E}
}var D,F,B,G;
for(var A in E){D=E[A];
A=A.toLowerCase();
F=Element._insertionTranslations[A];
if(D&&D.toElement){D=D.toElement()
}if(Object.isElement(D)){F(C,D);
continue
}D=Object.toHTML(D);
B=((A=="before"||A=="after")?C.parentNode:C).tagName.toUpperCase();
G=Element._getContentFromAnonymousElement(B,D.stripScripts());
if(A=="top"||A=="after"){G.reverse()
}G.each(F.curry(C));
D.evalScripts.bind(D).defer()
}return C
},wrap:function(B,C,A){B=$(B);
if(Object.isElement(C)){$(C).writeAttribute(A||{})
}else{if(Object.isString(C)){C=new Element(C,A)
}else{C=new Element("div",C)
}}if(B.parentNode){B.parentNode.replaceChild(C,B)
}C.appendChild(B);
return C
},inspect:function(B){B=$(B);
var A="<"+B.tagName.toLowerCase();
$H({id:"id",className:"class"}).each(function(F){var E=F.first(),C=F.last(),D=(B[E]||"").toString();
if(D){A+=" "+C+"="+D.inspect(true)
}});
return A+">"
},recursivelyCollect:function(A,C,D){A=$(A);
D=D||-1;
var B=[];
while(A=A[C]){if(A.nodeType==1){B.push(Element.extend(A))
}if(B.length==D){break
}}return B
},ancestors:function(A){return Element.recursivelyCollect(A,"parentNode")
},descendants:function(A){return Element.select(A,"*")
},firstDescendant:function(A){A=$(A).firstChild;
while(A&&A.nodeType!=1){A=A.nextSibling
}return $(A)
},immediateDescendants:function(B){var A=[],C=$(B).firstChild;
while(C){if(C.nodeType===1){A.push(Element.extend(C))
}C=C.nextSibling
}return A
},previousSiblings:function(A,B){return Element.recursivelyCollect(A,"previousSibling")
},nextSiblings:function(A){return Element.recursivelyCollect(A,"nextSibling")
},siblings:function(A){A=$(A);
return Element.previousSiblings(A).reverse().concat(Element.nextSiblings(A))
},match:function(B,A){B=$(B);
if(Object.isString(A)){return Prototype.Selector.match(B,A)
}return A.match(B)
},up:function(B,D,A){B=$(B);
if(arguments.length==1){return $(B.parentNode)
}var C=Element.ancestors(B);
return Object.isNumber(D)?C[D]:Prototype.Selector.find(C,D,A)
},down:function(B,C,A){B=$(B);
if(arguments.length==1){return Element.firstDescendant(B)
}return Object.isNumber(C)?Element.descendants(B)[C]:Element.select(B,C)[A||0]
},previous:function(B,C,A){B=$(B);
if(Object.isNumber(C)){A=C,C=false
}if(!Object.isNumber(A)){A=0
}if(C){return Prototype.Selector.find(B.previousSiblings(),C,A)
}else{return B.recursivelyCollect("previousSibling",A+1)[A]
}},next:function(B,D,A){B=$(B);
if(Object.isNumber(D)){A=D,D=false
}if(!Object.isNumber(A)){A=0
}if(D){return Prototype.Selector.find(B.nextSiblings(),D,A)
}else{var C=Object.isNumber(A)?A+1:1;
return B.recursivelyCollect("nextSibling",A+1)[A]
}},select:function(A){A=$(A);
var B=Array.prototype.slice.call(arguments,1).join(", ");
return Prototype.Selector.select(B,A)
},adjacent:function(A){A=$(A);
var B=Array.prototype.slice.call(arguments,1).join(", ");
return Prototype.Selector.select(B,A.parentNode).without(A)
},identify:function(A){A=$(A);
var B=Element.readAttribute(A,"id");
if(B){return B
}do{B="anonymous_element_"+Element.idCounter++
}while($(B));
Element.writeAttribute(A,"id",B);
return B
},readAttribute:function(C,A){C=$(C);
if(Prototype.Browser.IE){var B=Element._attributeTranslations.read;
if(B.values[A]){return B.values[A](C,A)
}if(B.names[A]){A=B.names[A]
}if(A.include(":")){return(!C.attributes||!C.attributes[A])?null:C.attributes[A].value
}}return C.getAttribute(A)
},writeAttribute:function(E,C,F){E=$(E);
var B={},D=Element._attributeTranslations.write;
if(typeof C=="object"){B=C
}else{B[C]=Object.isUndefined(F)?true:F
}for(var A in B){C=D.names[A]||A;
F=B[A];
if(D.values[A]){C=D.values[A](E,F)
}if(F===false||F===null){E.removeAttribute(C)
}else{if(F===true){E.setAttribute(C,C)
}else{E.setAttribute(C,F)
}}}return E
},getHeight:function(A){return Element.getDimensions(A).height
},getWidth:function(A){return Element.getDimensions(A).width
},classNames:function(A){return new Element.ClassNames(A)
},hasClassName:function(A,B){if(!(A=$(A))){return 
}var C=A.className;
return(C.length>0&&(C==B||new RegExp("(^|\\s)"+B+"(\\s|$)").test(C)))
},addClassName:function(A,B){if(!(A=$(A))){return 
}if(!Element.hasClassName(A,B)){A.className+=(A.className?" ":"")+B
}return A
},removeClassName:function(A,B){if(!(A=$(A))){return 
}A.className=A.className.replace(new RegExp("(^|\\s+)"+B+"(\\s+|$)")," ").strip();
return A
},toggleClassName:function(A,B){if(!(A=$(A))){return 
}return Element[Element.hasClassName(A,B)?"removeClassName":"addClassName"](A,B)
},cleanWhitespace:function(B){B=$(B);
var C=B.firstChild;
while(C){var A=C.nextSibling;
if(C.nodeType==3&&!/\S/.test(C.nodeValue)){B.removeChild(C)
}C=A
}return B
},empty:function(A){return $(A).innerHTML.blank()
},descendantOf:function(B,A){B=$(B),A=$(A);
if(B.compareDocumentPosition){return(B.compareDocumentPosition(A)&8)===8
}if(A.contains){return A.contains(B)&&A!==B
}while(B=B.parentNode){if(B==A){return true
}}return false
},scrollTo:function(A){A=$(A);
var B=Element.cumulativeOffset(A);
window.scrollTo(B[0],B[1]);
return A
},getStyle:function(B,C){B=$(B);
C=C=="float"?"cssFloat":C.camelize();
var D=B.style[C];
if(!D||D=="auto"){var A=document.defaultView.getComputedStyle(B,null);
D=A?A[C]:null
}if(C=="opacity"){return D?parseFloat(D):1
}return D=="auto"?null:D
},getOpacity:function(A){return $(A).getStyle("opacity")
},setStyle:function(B,C){B=$(B);
var E=B.style,A;
if(Object.isString(C)){B.style.cssText+=";"+C;
return C.include("opacity")?B.setOpacity(C.match(/opacity:\s*(\d?\.?\d*)/)[1]):B
}for(var D in C){if(D=="opacity"){B.setOpacity(C[D])
}else{E[(D=="float"||D=="cssFloat")?(Object.isUndefined(E.styleFloat)?"cssFloat":"styleFloat"):D]=C[D]
}}return B
},setOpacity:function(A,B){A=$(A);
A.style.opacity=(B==1||B==="")?"":(B<0.00001)?0:B;
return A
},makePositioned:function(A){A=$(A);
var B=Element.getStyle(A,"position");
if(B=="static"||!B){A._madePositioned=true;
A.style.position="relative";
if(Prototype.Browser.Opera){A.style.top=0;
A.style.left=0
}}return A
},undoPositioned:function(A){A=$(A);
if(A._madePositioned){A._madePositioned=undefined;
A.style.position=A.style.top=A.style.left=A.style.bottom=A.style.right=""
}return A
},makeClipping:function(A){A=$(A);
if(A._overflow){return A
}A._overflow=Element.getStyle(A,"overflow")||"auto";
if(A._overflow!=="hidden"){A.style.overflow="hidden"
}return A
},undoClipping:function(A){A=$(A);
if(!A._overflow){return A
}A.style.overflow=A._overflow=="auto"?"":A._overflow;
A._overflow=null;
return A
},clonePosition:function(B,D){var A=Object.extend({setLeft:true,setTop:true,setWidth:true,setHeight:true,offsetTop:0,offsetLeft:0},arguments[2]||{});
D=$(D);
var E=Element.viewportOffset(D),F=[0,0],C=null;
B=$(B);
if(Element.getStyle(B,"position")=="absolute"){C=Element.getOffsetParent(B);
F=Element.viewportOffset(C)
}if(C==document.body){F[0]-=document.body.offsetLeft;
F[1]-=document.body.offsetTop
}if(A.setLeft){B.style.left=(E[0]-F[0]+A.offsetLeft)+"px"
}if(A.setTop){B.style.top=(E[1]-F[1]+A.offsetTop)+"px"
}if(A.setWidth){B.style.width=D.offsetWidth+"px"
}if(A.setHeight){B.style.height=D.offsetHeight+"px"
}return B
}};
Object.extend(Element.Methods,{getElementsBySelector:Element.Methods.select,childElements:Element.Methods.immediateDescendants});
Element._attributeTranslations={write:{names:{className:"class",htmlFor:"for"},values:{}}};
if(Prototype.Browser.Opera){Element.Methods.getStyle=Element.Methods.getStyle.wrap(function(D,B,C){switch(C){case"height":case"width":if(!Element.visible(B)){return null
}var E=parseInt(D(B,C),10);
if(E!==B["offset"+C.capitalize()]){return E+"px"
}var A;
if(C==="height"){A=["border-top-width","padding-top","padding-bottom","border-bottom-width"]
}else{A=["border-left-width","padding-left","padding-right","border-right-width"]
}return A.inject(E,function(F,G){var H=D(B,G);
return H===null?F:F-parseInt(H,10)
})+"px";
default:return D(B,C)
}});
Element.Methods.readAttribute=Element.Methods.readAttribute.wrap(function(C,A,B){if(B==="title"){return A.title
}return C(A,B)
})
}else{if(Prototype.Browser.IE){Element.Methods.getStyle=function(A,B){A=$(A);
B=(B=="float"||B=="cssFloat")?"styleFloat":B.camelize();
var C=A.style[B];
if(!C&&A.currentStyle){C=A.currentStyle[B]
}if(B=="opacity"){if(C=(A.getStyle("filter")||"").match(/alpha\(opacity=(.*)\)/)){if(C[1]){return parseFloat(C[1])/100
}}return 1
}if(C=="auto"){if((B=="width"||B=="height")&&(A.getStyle("display")!="none")){return A["offset"+B.capitalize()]+"px"
}return null
}return C
};
Element.Methods.setOpacity=function(B,E){function F(G){return G.replace(/alpha\([^\)]*\)/gi,"")
}B=$(B);
var A=B.currentStyle;
if((A&&!A.hasLayout)||(!A&&B.style.zoom=="normal")){B.style.zoom=1
}var D=B.getStyle("filter"),C=B.style;
if(E==1||E===""){(D=F(D))?C.filter=D:C.removeAttribute("filter");
return B
}else{if(E<0.00001){E=0
}}C.filter=F(D)+"alpha(opacity="+(E*100)+")";
return B
};
Element._attributeTranslations=(function(){var B="className",A="for",C=document.createElement("div");
C.setAttribute(B,"x");
if(C.className!=="x"){C.setAttribute("class","x");
if(C.className==="x"){B="class"
}}C=null;
C=document.createElement("label");
C.setAttribute(A,"x");
if(C.htmlFor!=="x"){C.setAttribute("htmlFor","x");
if(C.htmlFor==="x"){A="htmlFor"
}}C=null;
return{read:{names:{"class":B,className:B,"for":A,htmlFor:A},values:{_getAttr:function(D,E){return D.getAttribute(E)
},_getAttr2:function(D,E){return D.getAttribute(E,2)
},_getAttrNode:function(D,F){var E=D.getAttributeNode(F);
return E?E.value:""
},_getEv:(function(){var D=document.createElement("div"),F;
D.onclick=Prototype.emptyFunction;
var E=D.getAttribute("onclick");
if(String(E).indexOf("{")>-1){F=function(G,H){H=G.getAttribute(H);
if(!H){return null
}H=H.toString();
H=H.split("{")[1];
H=H.split("}")[0];
return H.strip()
}
}else{if(E===""){F=function(G,H){H=G.getAttribute(H);
if(!H){return null
}return H.strip()
}
}}D=null;
return F
})(),_flag:function(D,E){return $(D).hasAttribute(E)?E:null
},style:function(D){return D.style.cssText.toLowerCase()
},title:function(D){return D.title
}}}}
})();
Element._attributeTranslations.write={names:Object.extend({cellpadding:"cellPadding",cellspacing:"cellSpacing"},Element._attributeTranslations.read.names),values:{checked:function(A,B){A.checked=!!B
},style:function(A,B){A.style.cssText=B?B:""
}}};
Element._attributeTranslations.has={};
$w("colSpan rowSpan vAlign dateTime accessKey tabIndex encType maxLength readOnly longDesc frameBorder").each(function(A){Element._attributeTranslations.write.names[A.toLowerCase()]=A;
Element._attributeTranslations.has[A.toLowerCase()]=A
});
(function(A){Object.extend(A,{href:A._getAttr2,src:A._getAttr2,type:A._getAttr,action:A._getAttrNode,disabled:A._flag,checked:A._flag,readonly:A._flag,multiple:A._flag,onload:A._getEv,onunload:A._getEv,onclick:A._getEv,ondblclick:A._getEv,onmousedown:A._getEv,onmouseup:A._getEv,onmouseover:A._getEv,onmousemove:A._getEv,onmouseout:A._getEv,onfocus:A._getEv,onblur:A._getEv,onkeypress:A._getEv,onkeydown:A._getEv,onkeyup:A._getEv,onsubmit:A._getEv,onreset:A._getEv,onselect:A._getEv,onchange:A._getEv})
})(Element._attributeTranslations.read.values);
if(Prototype.BrowserFeatures.ElementExtensions){(function(){function A(E){var B=E.getElementsByTagName("*"),D=[];
for(var C=0,F;
F=B[C];
C++){if(F.tagName!=="!"){D.push(F)
}}return D
}Element.Methods.down=function(C,D,B){C=$(C);
if(arguments.length==1){return C.firstDescendant()
}return Object.isNumber(D)?A(C)[D]:Element.select(C,D)[B||0]
}
})()
}}else{if(Prototype.Browser.Gecko&&/rv:1\.8\.0/.test(navigator.userAgent)){Element.Methods.setOpacity=function(A,B){A=$(A);
A.style.opacity=(B==1)?0.999999:(B==="")?"":(B<0.00001)?0:B;
return A
}
}else{if(Prototype.Browser.WebKit){Element.Methods.setOpacity=function(A,B){A=$(A);
A.style.opacity=(B==1||B==="")?"":(B<0.00001)?0:B;
if(B==1){if(A.tagName.toUpperCase()=="IMG"&&A.width){A.width++;
A.width--
}else{try{var D=document.createTextNode(" ");
A.appendChild(D);
A.removeChild(D)
}catch(C){}}}return A
}
}}}}if("outerHTML" in document.documentElement){Element.Methods.replace=function(C,E){C=$(C);
if(E&&E.toElement){E=E.toElement()
}if(Object.isElement(E)){C.parentNode.replaceChild(E,C);
return C
}E=Object.toHTML(E);
var D=C.parentNode,B=D.tagName.toUpperCase();
if(Element._insertionTranslations.tags[B]){var F=C.next(),A=Element._getContentFromAnonymousElement(B,E.stripScripts());
D.removeChild(C);
if(F){A.each(function(G){D.insertBefore(G,F)
})
}else{A.each(function(G){D.appendChild(G)
})
}}else{C.outerHTML=E.stripScripts()
}E.evalScripts.bind(E).defer();
return C
}
}Element._returnOffset=function(B,C){var A=[B,C];
A.left=B;
A.top=C;
return A
};
Element._getContentFromAnonymousElement=function(E,D,F){var G=new Element("div"),C=Element._insertionTranslations.tags[E];
var A=false;
if(C){A=true
}else{if(F){A=true;
C=["","",0]
}}if(A){G.innerHTML="&nbsp;"+C[0]+D+C[1];
G.removeChild(G.firstChild);
for(var B=C[2];
B--;
){G=G.firstChild
}}else{G.innerHTML=D
}return $A(G.childNodes)
};
Element._insertionTranslations={before:function(A,B){A.parentNode.insertBefore(B,A)
},top:function(A,B){A.insertBefore(B,A.firstChild)
},bottom:function(A,B){A.appendChild(B)
},after:function(A,B){A.parentNode.insertBefore(B,A.nextSibling)
},tags:{TABLE:["<table>","</table>",1],TBODY:["<table><tbody>","</tbody></table>",2],TR:["<table><tbody><tr>","</tr></tbody></table>",3],TD:["<table><tbody><tr><td>","</td></tr></tbody></table>",4],SELECT:["<select>","</select>",1]}};
(function(){var A=Element._insertionTranslations.tags;
Object.extend(A,{THEAD:A.TBODY,TFOOT:A.TBODY,TH:A.TD})
})();
Element.Methods.Simulated={hasAttribute:function(A,C){C=Element._attributeTranslations.has[C]||C;
var B=$(A).getAttributeNode(C);
return !!(B&&B.specified)
}};
Element.Methods.ByTag={};
Object.extend(Element,Element.Methods);
(function(A){if(!Prototype.BrowserFeatures.ElementExtensions&&A.__proto__){window.HTMLElement={};
window.HTMLElement.prototype=A.__proto__;
Prototype.BrowserFeatures.ElementExtensions=true
}A=null
})(document.createElement("div"));
Element.extend=(function(){function C(G){if(typeof window.Element!="undefined"){var I=window.Element.prototype;
if(I){var K="_"+(Math.random()+"").slice(2),H=document.createElement(G);
I[K]="x";
var J=(H[K]!=="x");
delete I[K];
H=null;
return J
}}return false
}function B(H,G){for(var J in G){var I=G[J];
if(Object.isFunction(I)&&!(J in H)){H[J]=I.methodize()
}}}var D=C("object");
if(Prototype.BrowserFeatures.SpecificElementExtensions){if(D){return function(H){if(H&&typeof H._extendedByPrototype=="undefined"){var G=H.tagName;
if(G&&(/^(?:object|applet|embed)$/i.test(G))){B(H,Element.Methods);
B(H,Element.Methods.Simulated);
B(H,Element.Methods.ByTag[G.toUpperCase()])
}}return H
}
}return Prototype.K
}var A={},E=Element.Methods.ByTag;
var F=Object.extend(function(I){if(!I||typeof I._extendedByPrototype!="undefined"||I.nodeType!=1||I==window){return I
}var G=Object.clone(A),H=I.tagName.toUpperCase();
if(E[H]){Object.extend(G,E[H])
}B(I,G);
I._extendedByPrototype=Prototype.emptyFunction;
return I
},{refresh:function(){if(!Prototype.BrowserFeatures.ElementExtensions){Object.extend(A,Element.Methods);
Object.extend(A,Element.Methods.Simulated)
}}});
F.refresh();
return F
})();
if(document.documentElement.hasAttribute){Element.hasAttribute=function(A,B){return A.hasAttribute(B)
}
}else{Element.hasAttribute=Element.Methods.Simulated.hasAttribute
}Element.addMethods=function(C){var J=Prototype.BrowserFeatures,D=Element.Methods.ByTag;
if(!C){Object.extend(Form,Form.Methods);
Object.extend(Form.Element,Form.Element.Methods);
Object.extend(Element.Methods.ByTag,{FORM:Object.clone(Form.Methods),INPUT:Object.clone(Form.Element.Methods),SELECT:Object.clone(Form.Element.Methods),TEXTAREA:Object.clone(Form.Element.Methods),BUTTON:Object.clone(Form.Element.Methods)})
}if(arguments.length==2){var B=C;
C=arguments[1]
}if(!B){Object.extend(Element.Methods,C||{})
}else{if(Object.isArray(B)){B.each(H)
}else{H(B)
}}function H(F){F=F.toUpperCase();
if(!Element.Methods.ByTag[F]){Element.Methods.ByTag[F]={}
}Object.extend(Element.Methods.ByTag[F],C)
}function A(M,L,F){F=F||false;
for(var O in M){var N=M[O];
if(!Object.isFunction(N)){continue
}if(!F||!(O in L)){L[O]=N.methodize()
}}}function E(N){var F;
var M={OPTGROUP:"OptGroup",TEXTAREA:"TextArea",P:"Paragraph",FIELDSET:"FieldSet",UL:"UList",OL:"OList",DL:"DList",DIR:"Directory",H1:"Heading",H2:"Heading",H3:"Heading",H4:"Heading",H5:"Heading",H6:"Heading",Q:"Quote",INS:"Mod",DEL:"Mod",A:"Anchor",IMG:"Image",CAPTION:"TableCaption",COL:"TableCol",COLGROUP:"TableCol",THEAD:"TableSection",TFOOT:"TableSection",TBODY:"TableSection",TR:"TableRow",TH:"TableCell",TD:"TableCell",FRAMESET:"FrameSet",IFRAME:"IFrame"};
if(M[N]){F="HTML"+M[N]+"Element"
}if(window[F]){return window[F]
}F="HTML"+N+"Element";
if(window[F]){return window[F]
}F="HTML"+N.capitalize()+"Element";
if(window[F]){return window[F]
}var L=document.createElement(N),O=L.__proto__||L.constructor.prototype;
L=null;
return O
}var I=window.HTMLElement?HTMLElement.prototype:Element.prototype;
if(J.ElementExtensions){A(Element.Methods,I);
A(Element.Methods.Simulated,I,true)
}if(J.SpecificElementExtensions){for(var K in Element.Methods.ByTag){var G=E(K);
if(Object.isUndefined(G)){continue
}A(D[K],G.prototype)
}}Object.extend(Element,Element.Methods);
delete Element.ByTag;
if(Element.extend.refresh){Element.extend.refresh()
}Element.cache={}
};
document.viewport={getDimensions:function(){return{width:this.getWidth(),height:this.getHeight()}
},getScrollOffsets:function(){return Element._returnOffset(window.pageXOffset||document.documentElement.scrollLeft||document.body.scrollLeft,window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop)
}};
(function(C){var H=Prototype.Browser,F=document,D,E={};
function A(){if(H.WebKit&&!F.evaluate){return document
}if(H.Opera&&window.parseFloat(window.opera.version())<9.5){return document.body
}return document.documentElement
}function G(B){if(!D){D=A()
}E[B]="client"+B;
C["get"+B]=function(){return D[E[B]]
};
return C["get"+B]()
}C.getWidth=G.curry("Width");
C.getHeight=G.curry("Height")
})(document.viewport);
Element.Storage={UID:1};
Element.addMethods({getStorage:function(B){if(!(B=$(B))){return 
}var A;
if(B===window){A=0
}else{if(typeof B._prototypeUID==="undefined"){B._prototypeUID=Element.Storage.UID++
}A=B._prototypeUID
}if(!Element.Storage[A]){Element.Storage[A]=$H()
}return Element.Storage[A]
},store:function(B,A,C){if(!(B=$(B))){return 
}if(arguments.length===2){Element.getStorage(B).update(A)
}else{Element.getStorage(B).set(A,C)
}return B
},retrieve:function(C,B,A){if(!(C=$(C))){return 
}var E=Element.getStorage(C),D=E.get(B);
if(Object.isUndefined(D)){E.set(B,A);
D=A
}return D
},clone:function(C,A){if(!(C=$(C))){return 
}var E=C.cloneNode(A);
E._prototypeUID=void 0;
if(A){var D=Element.select(E,"*"),B=D.length;
while(B--){D[B]._prototypeUID=void 0
}}return Element.extend(E)
},purge:function(C){if(!(C=$(C))){return 
}var A=Element._purgeElement;
A(C);
var D=C.getElementsByTagName("*"),B=D.length;
while(B--){A(D[B])
}return null
}});
(function(){function H(V){var U=V.match(/^(\d+)%?$/i);
if(!U){return null
}return(Number(U[1])/100)
}function O(f,g,V){var Y=null;
if(Object.isElement(f)){Y=f;
f=Y.getStyle(g)
}if(f===null){return null
}if((/^(?:-)?\d+(\.\d+)?(px)?$/i).test(f)){return window.parseFloat(f)
}var a=f.include("%"),W=(V===document.viewport);
if(/\d/.test(f)&&Y&&Y.runtimeStyle&&!(a&&W)){var U=Y.style.left,e=Y.runtimeStyle.left;
Y.runtimeStyle.left=Y.currentStyle.left;
Y.style.left=f||0;
f=Y.style.pixelLeft;
Y.style.left=U;
Y.runtimeStyle.left=e;
return f
}if(Y&&a){V=V||Y.parentNode;
var X=H(f);
var b=null;
var Z=Y.getStyle("position");
var d=g.include("left")||g.include("right")||g.include("width");
var c=g.include("top")||g.include("bottom")||g.include("height");
if(V===document.viewport){if(d){b=document.viewport.getWidth()
}else{if(c){b=document.viewport.getHeight()
}}}else{if(d){b=$(V).measure("width")
}else{if(c){b=$(V).measure("height")
}}}return(b===null)?0:b*X
}return 0
}function G(U){if(Object.isString(U)&&U.endsWith("px")){return U
}return U+"px"
}function J(V){var U=V;
while(V&&V.parentNode){var W=V.getStyle("display");
if(W==="none"){return false
}V=$(V.parentNode)
}return true
}var D=Prototype.K;
if("currentStyle" in document.documentElement){D=function(U){if(!U.currentStyle.hasLayout){U.style.zoom=1
}return U
}
}function F(U){if(U.include("border")){U=U+"-width"
}return U.camelize()
}Element.Layout=Class.create(Hash,{initialize:function($super,V,U){$super();
this.element=$(V);
Element.Layout.PROPERTIES.each(function(W){this._set(W,null)
},this);
if(U){this._preComputing=true;
this._begin();
Element.Layout.PROPERTIES.each(this._compute,this);
this._end();
this._preComputing=false
}},_set:function(V,U){return Hash.prototype.set.call(this,V,U)
},set:function(V,U){throw"Properties of Element.Layout are read-only."
},get:function($super,V){var U=$super(V);
return U===null?this._compute(V):U
},_begin:function(){if(this._prepared){return 
}var Y=this.element;
if(J(Y)){this._prepared=true;
return 
}var a={position:Y.style.position||"",width:Y.style.width||"",visibility:Y.style.visibility||"",display:Y.style.display||""};
Y.store("prototype_original_styles",a);
var b=Y.getStyle("position"),U=Y.getStyle("width");
if(U==="0px"||U===null){Y.style.display="block";
U=Y.getStyle("width")
}var V=(b==="fixed")?document.viewport:Y.parentNode;
Y.setStyle({position:"absolute",visibility:"hidden",display:"block"});
var W=Y.getStyle("width");
var X;
if(U&&(W===U)){X=O(Y,"width",V)
}else{if(b==="absolute"||b==="fixed"){X=O(Y,"width",V)
}else{var c=Y.parentNode,Z=$(c).getLayout();
X=Z.get("width")-this.get("margin-left")-this.get("border-left")-this.get("padding-left")-this.get("padding-right")-this.get("border-right")-this.get("margin-right")
}}Y.setStyle({width:X+"px"});
this._prepared=true
},_end:function(){var V=this.element;
var U=V.retrieve("prototype_original_styles");
V.store("prototype_original_styles",null);
V.setStyle(U);
this._prepared=false
},_compute:function(V){var U=Element.Layout.COMPUTATIONS;
if(!(V in U)){throw"Property not found."
}return this._set(V,U[V].call(this,this.element))
},toObject:function(){var U=$A(arguments);
var V=(U.length===0)?Element.Layout.PROPERTIES:U.join(" ").split(" ");
var W={};
V.each(function(X){if(!Element.Layout.PROPERTIES.include(X)){return 
}var Y=this.get(X);
if(Y!=null){W[X]=Y
}},this);
return W
},toHash:function(){var U=this.toObject.apply(this,arguments);
return new Hash(U)
},toCSS:function(){var U=$A(arguments);
var W=(U.length===0)?Element.Layout.PROPERTIES:U.join(" ").split(" ");
var V={};
W.each(function(X){if(!Element.Layout.PROPERTIES.include(X)){return 
}if(Element.Layout.COMPOSITE_PROPERTIES.include(X)){return 
}var Y=this.get(X);
if(Y!=null){V[F(X)]=Y+"px"
}},this);
return V
},inspect:function(){return"#<Element.Layout>"
}});
Object.extend(Element.Layout,{PROPERTIES:$w("height width top left right bottom border-left border-right border-top border-bottom padding-left padding-right padding-top padding-bottom margin-top margin-bottom margin-left margin-right padding-box-width padding-box-height border-box-width border-box-height margin-box-width margin-box-height"),COMPOSITE_PROPERTIES:$w("padding-box-width padding-box-height margin-box-width margin-box-height border-box-width border-box-height"),COMPUTATIONS:{height:function(W){if(!this._preComputing){this._begin()
}var U=this.get("border-box-height");
if(U<=0){if(!this._preComputing){this._end()
}return 0
}var X=this.get("border-top"),V=this.get("border-bottom");
var Z=this.get("padding-top"),Y=this.get("padding-bottom");
if(!this._preComputing){this._end()
}return U-X-V-Z-Y
},width:function(W){if(!this._preComputing){this._begin()
}var V=this.get("border-box-width");
if(V<=0){if(!this._preComputing){this._end()
}return 0
}var Z=this.get("border-left"),U=this.get("border-right");
var X=this.get("padding-left"),Y=this.get("padding-right");
if(!this._preComputing){this._end()
}return V-Z-U-X-Y
},"padding-box-height":function(V){var U=this.get("height"),X=this.get("padding-top"),W=this.get("padding-bottom");
return U+X+W
},"padding-box-width":function(U){var V=this.get("width"),W=this.get("padding-left"),X=this.get("padding-right");
return V+W+X
},"border-box-height":function(V){if(!this._preComputing){this._begin()
}var U=V.offsetHeight;
if(!this._preComputing){this._end()
}return U
},"border-box-width":function(U){if(!this._preComputing){this._begin()
}var V=U.offsetWidth;
if(!this._preComputing){this._end()
}return V
},"margin-box-height":function(V){var U=this.get("border-box-height"),W=this.get("margin-top"),X=this.get("margin-bottom");
if(U<=0){return 0
}return U+W+X
},"margin-box-width":function(W){var V=this.get("border-box-width"),X=this.get("margin-left"),U=this.get("margin-right");
if(V<=0){return 0
}return V+X+U
},top:function(U){var V=U.positionedOffset();
return V.top
},bottom:function(U){var X=U.positionedOffset(),V=U.getOffsetParent(),W=V.measure("height");
var Y=this.get("border-box-height");
return W-Y-X.top
},left:function(U){var V=U.positionedOffset();
return V.left
},right:function(W){var Y=W.positionedOffset(),X=W.getOffsetParent(),U=X.measure("width");
var V=this.get("border-box-width");
return U-V-Y.left
},"padding-top":function(U){return O(U,"paddingTop")
},"padding-bottom":function(U){return O(U,"paddingBottom")
},"padding-left":function(U){return O(U,"paddingLeft")
},"padding-right":function(U){return O(U,"paddingRight")
},"border-top":function(U){return O(U,"borderTopWidth")
},"border-bottom":function(U){return O(U,"borderBottomWidth")
},"border-left":function(U){return O(U,"borderLeftWidth")
},"border-right":function(U){return O(U,"borderRightWidth")
},"margin-top":function(U){return O(U,"marginTop")
},"margin-bottom":function(U){return O(U,"marginBottom")
},"margin-left":function(U){return O(U,"marginLeft")
},"margin-right":function(U){return O(U,"marginRight")
}}});
if("getBoundingClientRect" in document.documentElement){Object.extend(Element.Layout.COMPUTATIONS,{right:function(V){var W=D(V.getOffsetParent());
var X=V.getBoundingClientRect(),U=W.getBoundingClientRect();
return(U.right-X.right).round()
},bottom:function(V){var W=D(V.getOffsetParent());
var X=V.getBoundingClientRect(),U=W.getBoundingClientRect();
return(U.bottom-X.bottom).round()
}})
}Element.Offset=Class.create({initialize:function(V,U){this.left=V.round();
this.top=U.round();
this[0]=this.left;
this[1]=this.top
},relativeTo:function(U){return new Element.Offset(this.left-U.left,this.top-U.top)
},inspect:function(){return"#<Element.Offset left: #{left} top: #{top}>".interpolate(this)
},toString:function(){return"[#{left}, #{top}]".interpolate(this)
},toArray:function(){return[this.left,this.top]
}});
function R(V,U){return new Element.Layout(V,U)
}function B(U,V){return $(U).getLayout().get(V)
}function N(V){V=$(V);
var Z=Element.getStyle(V,"display");
if(Z&&Z!=="none"){return{width:V.offsetWidth,height:V.offsetHeight}
}var W=V.style;
var U={visibility:W.visibility,position:W.position,display:W.display};
var Y={visibility:"hidden",display:"block"};
if(U.position!=="fixed"){Y.position="absolute"
}Element.setStyle(V,Y);
var X={width:V.offsetWidth,height:V.offsetHeight};
Element.setStyle(V,U);
return X
}function L(U){U=$(U);
if(E(U)||C(U)||M(U)||K(U)){return $(document.body)
}var V=(Element.getStyle(U,"display")==="inline");
if(!V&&U.offsetParent){return $(U.offsetParent)
}while((U=U.parentNode)&&U!==document.body){if(Element.getStyle(U,"position")!=="static"){return K(U)?$(document.body):$(U)
}}return $(document.body)
}function T(V){V=$(V);
var U=0,W=0;
if(V.parentNode){do{U+=V.offsetTop||0;
W+=V.offsetLeft||0;
V=V.offsetParent
}while(V)
}return new Element.Offset(W,U)
}function P(V){V=$(V);
var W=V.getLayout();
var U=0,Y=0;
do{U+=V.offsetTop||0;
Y+=V.offsetLeft||0;
V=V.offsetParent;
if(V){if(M(V)){break
}var X=Element.getStyle(V,"position");
if(X!=="static"){break
}}}while(V);
Y-=W.get("margin-top");
U-=W.get("margin-left");
return new Element.Offset(Y,U)
}function A(V){var U=0,W=0;
do{U+=V.scrollTop||0;
W+=V.scrollLeft||0;
V=V.parentNode
}while(V);
return new Element.Offset(W,U)
}function S(Y){V=$(V);
var U=0,X=0,W=document.body;
var V=Y;
do{U+=V.offsetTop||0;
X+=V.offsetLeft||0;
if(V.offsetParent==W&&Element.getStyle(V,"position")=="absolute"){break
}}while(V=V.offsetParent);
V=Y;
do{if(V!=W){U-=V.scrollTop||0;
X-=V.scrollLeft||0
}}while(V=V.parentNode);
return new Element.Offset(X,U)
}function Q(U){U=$(U);
if(Element.getStyle(U,"position")==="absolute"){return U
}var Y=L(U);
var X=U.viewportOffset(),V=Y.viewportOffset();
var Z=X.relativeTo(V);
var W=U.getLayout();
U.store("prototype_absolutize_original_styles",{left:U.getStyle("left"),top:U.getStyle("top"),width:U.getStyle("width"),height:U.getStyle("height")});
U.setStyle({position:"absolute",top:Z.top+"px",left:Z.left+"px",width:W.get("width")+"px",height:W.get("height")+"px"});
return U
}function I(V){V=$(V);
if(Element.getStyle(V,"position")==="relative"){return V
}var U=V.retrieve("prototype_absolutize_original_styles");
if(U){V.setStyle(U)
}return V
}if(Prototype.Browser.IE){L=L.wrap(function(W,V){V=$(V);
if(E(V)||C(V)||M(V)||K(V)){return $(document.body)
}var U=V.getStyle("position");
if(U!=="static"){return W(V)
}V.setStyle({position:"relative"});
var X=W(V);
V.setStyle({position:U});
return X
});
P=P.wrap(function(X,V){V=$(V);
if(!V.parentNode){return new Element.Offset(0,0)
}var U=V.getStyle("position");
if(U!=="static"){return X(V)
}var W=V.getOffsetParent();
if(W&&W.getStyle("position")==="fixed"){D(W)
}V.setStyle({position:"relative"});
var Y=X(V);
V.setStyle({position:U});
return Y
})
}else{if(Prototype.Browser.Webkit){T=function(V){V=$(V);
var U=0,W=0;
do{U+=V.offsetTop||0;
W+=V.offsetLeft||0;
if(V.offsetParent==document.body){if(Element.getStyle(V,"position")=="absolute"){break
}}V=V.offsetParent
}while(V);
return new Element.Offset(W,U)
}
}}Element.addMethods({getLayout:R,measure:B,getDimensions:N,getOffsetParent:L,cumulativeOffset:T,positionedOffset:P,cumulativeScrollOffset:A,viewportOffset:S,absolutize:Q,relativize:I});
function M(U){return U.nodeName.toUpperCase()==="BODY"
}function K(U){return U.nodeName.toUpperCase()==="HTML"
}function E(U){return U.nodeType===Node.DOCUMENT_NODE
}function C(U){return U!==document.body&&!Element.descendantOf(U,document.body)
}if("getBoundingClientRect" in document.documentElement){Element.addMethods({viewportOffset:function(U){U=$(U);
if(C(U)){return new Element.Offset(0,0)
}var V=U.getBoundingClientRect(),W=document.documentElement;
return new Element.Offset(V.left-W.clientLeft,V.top-W.clientTop)
}})
}})();
window.$$=function(){var A=$A(arguments).join(", ");
return Prototype.Selector.select(A,document)
};
Prototype.Selector=(function(){function A(){throw new Error('Method "Prototype.Selector.select" must be defined.')
}function C(){throw new Error('Method "Prototype.Selector.match" must be defined.')
}function D(K,L,H){H=H||0;
var G=Prototype.Selector.match,J=K.length,F=0,I;
for(I=0;
I<J;
I++){if(G(K[I],L)&&H==F++){return Element.extend(K[I])
}}}function E(H){for(var F=0,G=H.length;
F<G;
F++){Element.extend(H[F])
}return H
}var B=Prototype.K;
return{select:A,match:C,find:D,extendElements:(Element.extend===B)?B:E,extendElement:Element.extend}
})();
/*
 * Sizzle CSS Selector Engine - v1.0
 *  Copyright 2009, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 */
(function(){var P=/((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^[\]]*\]|['"][^'"]*['"]|[^[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g,I=0,D=Object.prototype.toString,N=false,H=true;
[0,0].sort(function(){H=false;
return 0
});
var B=function(e,U,b,W){b=b||[];
var R=U=U||document;
if(U.nodeType!==1&&U.nodeType!==9){return[]
}if(!e||typeof e!=="string"){return b
}var c=[],d,Z,j,h,a,T,S=true,X=O(U),g=e;
while((P.exec(""),d=P.exec(g))!==null){g=d[3];
c.push(d[1]);
if(d[2]){T=d[3];
break
}}if(c.length>1&&J.exec(e)){if(c.length===2&&E.relative[c[0]]){Z=F(c[0]+c[1],U)
}else{Z=E.relative[c[0]]?[U]:B(c.shift(),U);
while(c.length){e=c.shift();
if(E.relative[e]){e+=c.shift()
}Z=F(e,Z)
}}}else{if(!W&&c.length>1&&U.nodeType===9&&!X&&E.match.ID.test(c[0])&&!E.match.ID.test(c[c.length-1])){var k=B.find(c.shift(),U,X);
U=k.expr?B.filter(k.expr,k.set)[0]:k.set[0]
}if(U){var k=W?{expr:c.pop(),set:A(W)}:B.find(c.pop(),c.length===1&&(c[0]==="~"||c[0]==="+")&&U.parentNode?U.parentNode:U,X);
Z=k.expr?B.filter(k.expr,k.set):k.set;
if(c.length>0){j=A(Z)
}else{S=false
}while(c.length){var V=c.pop(),Y=V;
if(!E.relative[V]){V=""
}else{Y=c.pop()
}if(Y==null){Y=U
}E.relative[V](j,Y,X)
}}else{j=c=[]
}}if(!j){j=Z
}if(!j){throw"Syntax error, unrecognized expression: "+(V||e)
}if(D.call(j)==="[object Array]"){if(!S){b.push.apply(b,j)
}else{if(U&&U.nodeType===1){for(var f=0;
j[f]!=null;
f++){if(j[f]&&(j[f]===true||j[f].nodeType===1&&G(U,j[f]))){b.push(Z[f])
}}}else{for(var f=0;
j[f]!=null;
f++){if(j[f]&&j[f].nodeType===1){b.push(Z[f])
}}}}}else{A(j,b)
}if(T){B(T,R,b,W);
B.uniqueSort(b)
}return b
};
B.uniqueSort=function(S){if(C){N=H;
S.sort(C);
if(N){for(var R=1;
R<S.length;
R++){if(S[R]===S[R-1]){S.splice(R--,1)
}}}}return S
};
B.matches=function(R,S){return B(R,null,null,S)
};
B.find=function(Y,R,Z){var X,V;
if(!Y){return[]
}for(var U=0,T=E.order.length;
U<T;
U++){var W=E.order[U],V;
if((V=E.leftMatch[W].exec(Y))){var S=V[1];
V.splice(1,1);
if(S.substr(S.length-1)!=="\\"){V[1]=(V[1]||"").replace(/\\/g,"");
X=E.find[W](V,R,Z);
if(X!=null){Y=Y.replace(E.match[W],"");
break
}}}}if(!X){X=R.getElementsByTagName("*")
}return{set:X,expr:Y}
};
B.filter=function(b,a,e,U){var T=b,g=[],Y=a,W,R,X=a&&a[0]&&O(a[0]);
while(b&&a.length){for(var Z in E.filter){if((W=E.match[Z].exec(b))!=null){var S=E.filter[Z],f,d;
R=false;
if(Y==g){g=[]
}if(E.preFilter[Z]){W=E.preFilter[Z](W,Y,e,g,U,X);
if(!W){R=f=true
}else{if(W===true){continue
}}}if(W){for(var V=0;
(d=Y[V])!=null;
V++){if(d){f=S(d,W,V,Y);
var c=U^!!f;
if(e&&f!=null){if(c){R=true
}else{Y[V]=false
}}else{if(c){g.push(d);
R=true
}}}}}if(f!==undefined){if(!e){Y=g
}b=b.replace(E.match[Z],"");
if(!R){return[]
}break
}}}if(b==T){if(R==null){throw"Syntax error, unrecognized expression: "+b
}else{break
}}T=b
}return Y
};
var E=B.selectors={order:["ID","NAME","TAG"],match:{ID:/#((?:[\w\u00c0-\uFFFF-]|\\.)+)/,CLASS:/\.((?:[\w\u00c0-\uFFFF-]|\\.)+)/,NAME:/\[name=['"]*((?:[\w\u00c0-\uFFFF-]|\\.)+)['"]*\]/,ATTR:/\[\s*((?:[\w\u00c0-\uFFFF-]|\\.)+)\s*(?:(\S?=)\s*(['"]*)(.*?)\3|)\s*\]/,TAG:/^((?:[\w\u00c0-\uFFFF\*-]|\\.)+)/,CHILD:/:(only|nth|last|first)-child(?:\((even|odd|[\dn+-]*)\))?/,POS:/:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^-]|$)/,PSEUDO:/:((?:[\w\u00c0-\uFFFF-]|\\.)+)(?:\((['"]*)((?:\([^\)]+\)|[^\2\(\)]*)+)\2\))?/},leftMatch:{},attrMap:{"class":"className","for":"htmlFor"},attrHandle:{href:function(R){return R.getAttribute("href")
}},relative:{"+":function(Y,R,X){var V=typeof R==="string",Z=V&&!/\W/.test(R),W=V&&!Z;
if(Z&&!X){R=R.toUpperCase()
}for(var U=0,T=Y.length,S;
U<T;
U++){if((S=Y[U])){while((S=S.previousSibling)&&S.nodeType!==1){}Y[U]=W||S&&S.nodeName===R?S||false:S===R
}}if(W){B.filter(R,Y,true)
}},">":function(X,S,Y){var V=typeof S==="string";
if(V&&!/\W/.test(S)){S=Y?S:S.toUpperCase();
for(var T=0,R=X.length;
T<R;
T++){var W=X[T];
if(W){var U=W.parentNode;
X[T]=U.nodeName===S?U:false
}}}else{for(var T=0,R=X.length;
T<R;
T++){var W=X[T];
if(W){X[T]=V?W.parentNode:W.parentNode===S
}}if(V){B.filter(S,X,true)
}}},"":function(U,S,W){var T=I++,R=Q;
if(!/\W/.test(S)){var V=S=W?S:S.toUpperCase();
R=M
}R("parentNode",S,T,U,V,W)
},"~":function(U,S,W){var T=I++,R=Q;
if(typeof S==="string"&&!/\W/.test(S)){var V=S=W?S:S.toUpperCase();
R=M
}R("previousSibling",S,T,U,V,W)
}},find:{ID:function(S,T,U){if(typeof T.getElementById!=="undefined"&&!U){var R=T.getElementById(S[1]);
return R?[R]:[]
}},NAME:function(T,W,X){if(typeof W.getElementsByName!=="undefined"){var S=[],V=W.getElementsByName(T[1]);
for(var U=0,R=V.length;
U<R;
U++){if(V[U].getAttribute("name")===T[1]){S.push(V[U])
}}return S.length===0?null:S
}},TAG:function(R,S){return S.getElementsByTagName(R[1])
}},preFilter:{CLASS:function(U,S,T,R,X,Y){U=" "+U[1].replace(/\\/g,"")+" ";
if(Y){return U
}for(var V=0,W;
(W=S[V])!=null;
V++){if(W){if(X^(W.className&&(" "+W.className+" ").indexOf(U)>=0)){if(!T){R.push(W)
}}else{if(T){S[V]=false
}}}}return false
},ID:function(R){return R[1].replace(/\\/g,"")
},TAG:function(S,R){for(var T=0;
R[T]===false;
T++){}return R[T]&&O(R[T])?S[1]:S[1].toUpperCase()
},CHILD:function(R){if(R[1]=="nth"){var S=/(-?)(\d*)n((?:\+|-)?\d*)/.exec(R[2]=="even"&&"2n"||R[2]=="odd"&&"2n+1"||!/\D/.test(R[2])&&"0n+"+R[2]||R[2]);
R[2]=(S[1]+(S[2]||1))-0;
R[3]=S[3]-0
}R[0]=I++;
return R
},ATTR:function(V,S,T,R,W,X){var U=V[1].replace(/\\/g,"");
if(!X&&E.attrMap[U]){V[1]=E.attrMap[U]
}if(V[2]==="~="){V[4]=" "+V[4]+" "
}return V
},PSEUDO:function(V,S,T,R,W){if(V[1]==="not"){if((P.exec(V[3])||"").length>1||/^\w/.test(V[3])){V[3]=B(V[3],null,null,S)
}else{var U=B.filter(V[3],S,T,true^W);
if(!T){R.push.apply(R,U)
}return false
}}else{if(E.match.POS.test(V[0])||E.match.CHILD.test(V[0])){return true
}}return V
},POS:function(R){R.unshift(true);
return R
}},filters:{enabled:function(R){return R.disabled===false&&R.type!=="hidden"
},disabled:function(R){return R.disabled===true
},checked:function(R){return R.checked===true
},selected:function(R){R.parentNode.selectedIndex;
return R.selected===true
},parent:function(R){return !!R.firstChild
},empty:function(R){return !R.firstChild
},has:function(T,S,R){return !!B(R[3],T).length
},header:function(R){return/h\d/i.test(R.nodeName)
},text:function(R){return"text"===R.type
},radio:function(R){return"radio"===R.type
},checkbox:function(R){return"checkbox"===R.type
},file:function(R){return"file"===R.type
},password:function(R){return"password"===R.type
},submit:function(R){return"submit"===R.type
},image:function(R){return"image"===R.type
},reset:function(R){return"reset"===R.type
},button:function(R){return"button"===R.type||R.nodeName.toUpperCase()==="BUTTON"
},input:function(R){return/input|select|textarea|button/i.test(R.nodeName)
}},setFilters:{first:function(S,R){return R===0
},last:function(T,S,R,U){return S===U.length-1
},even:function(S,R){return R%2===0
},odd:function(S,R){return R%2===1
},lt:function(T,S,R){return S<R[3]-0
},gt:function(T,S,R){return S>R[3]-0
},nth:function(T,S,R){return R[3]-0==S
},eq:function(T,S,R){return R[3]-0==S
}},filter:{PSEUDO:function(X,T,U,Y){var S=T[1],V=E.filters[S];
if(V){return V(X,U,T,Y)
}else{if(S==="contains"){return(X.textContent||X.innerText||"").indexOf(T[3])>=0
}else{if(S==="not"){var W=T[3];
for(var U=0,R=W.length;
U<R;
U++){if(W[U]===X){return false
}}return true
}}}},CHILD:function(R,U){var X=U[1],S=R;
switch(X){case"only":case"first":while((S=S.previousSibling)){if(S.nodeType===1){return false
}}if(X=="first"){return true
}S=R;
case"last":while((S=S.nextSibling)){if(S.nodeType===1){return false
}}return true;
case"nth":var T=U[2],a=U[3];
if(T==1&&a==0){return true
}var W=U[0],Z=R.parentNode;
if(Z&&(Z.sizcache!==W||!R.nodeIndex)){var V=0;
for(S=Z.firstChild;
S;
S=S.nextSibling){if(S.nodeType===1){S.nodeIndex=++V
}}Z.sizcache=W
}var Y=R.nodeIndex-a;
if(T==0){return Y==0
}else{return(Y%T==0&&Y/T>=0)
}}},ID:function(S,R){return S.nodeType===1&&S.getAttribute("id")===R
},TAG:function(S,R){return(R==="*"&&S.nodeType===1)||S.nodeName===R
},CLASS:function(S,R){return(" "+(S.className||S.getAttribute("class"))+" ").indexOf(R)>-1
},ATTR:function(W,U){var T=U[1],R=E.attrHandle[T]?E.attrHandle[T](W):W[T]!=null?W[T]:W.getAttribute(T),X=R+"",V=U[2],S=U[4];
return R==null?V==="!=":V==="="?X===S:V==="*="?X.indexOf(S)>=0:V==="~="?(" "+X+" ").indexOf(S)>=0:!S?X&&R!==false:V==="!="?X!=S:V==="^="?X.indexOf(S)===0:V==="$="?X.substr(X.length-S.length)===S:V==="|="?X===S||X.substr(0,S.length+1)===S+"-":false
},POS:function(V,S,T,W){var R=S[2],U=E.setFilters[R];
if(U){return U(V,T,S,W)
}}}};
var J=E.match.POS;
for(var L in E.match){E.match[L]=new RegExp(E.match[L].source+/(?![^\[]*\])(?![^\(]*\))/.source);
E.leftMatch[L]=new RegExp(/(^(?:.|\r|\n)*?)/.source+E.match[L].source)
}var A=function(S,R){S=Array.prototype.slice.call(S,0);
if(R){R.push.apply(R,S);
return R
}return S
};
try{Array.prototype.slice.call(document.documentElement.childNodes,0)
}catch(K){A=function(V,U){var S=U||[];
if(D.call(V)==="[object Array]"){Array.prototype.push.apply(S,V)
}else{if(typeof V.length==="number"){for(var T=0,R=V.length;
T<R;
T++){S.push(V[T])
}}else{for(var T=0;
V[T];
T++){S.push(V[T])
}}}return S
}
}var C;
if(document.documentElement.compareDocumentPosition){C=function(S,R){if(!S.compareDocumentPosition||!R.compareDocumentPosition){if(S==R){N=true
}return 0
}var T=S.compareDocumentPosition(R)&4?-1:S===R?0:1;
if(T===0){N=true
}return T
}
}else{if("sourceIndex" in document.documentElement){C=function(S,R){if(!S.sourceIndex||!R.sourceIndex){if(S==R){N=true
}return 0
}var T=S.sourceIndex-R.sourceIndex;
if(T===0){N=true
}return T
}
}else{if(document.createRange){C=function(U,S){if(!U.ownerDocument||!S.ownerDocument){if(U==S){N=true
}return 0
}var T=U.ownerDocument.createRange(),R=S.ownerDocument.createRange();
T.setStart(U,0);
T.setEnd(U,0);
R.setStart(S,0);
R.setEnd(S,0);
var V=T.compareBoundaryPoints(Range.START_TO_END,R);
if(V===0){N=true
}return V
}
}}}(function(){var S=document.createElement("div"),T="script"+(new Date).getTime();
S.innerHTML="<a name='"+T+"'/>";
var R=document.documentElement;
R.insertBefore(S,R.firstChild);
if(!!document.getElementById(T)){E.find.ID=function(V,W,X){if(typeof W.getElementById!=="undefined"&&!X){var U=W.getElementById(V[1]);
return U?U.id===V[1]||typeof U.getAttributeNode!=="undefined"&&U.getAttributeNode("id").nodeValue===V[1]?[U]:undefined:[]
}};
E.filter.ID=function(W,U){var V=typeof W.getAttributeNode!=="undefined"&&W.getAttributeNode("id");
return W.nodeType===1&&V&&V.nodeValue===U
}
}R.removeChild(S);
R=S=null
})();
(function(){var R=document.createElement("div");
R.appendChild(document.createComment(""));
if(R.getElementsByTagName("*").length>0){E.find.TAG=function(S,W){var V=W.getElementsByTagName(S[1]);
if(S[1]==="*"){var U=[];
for(var T=0;
V[T];
T++){if(V[T].nodeType===1){U.push(V[T])
}}V=U
}return V
}
}R.innerHTML="<a href='#'></a>";
if(R.firstChild&&typeof R.firstChild.getAttribute!=="undefined"&&R.firstChild.getAttribute("href")!=="#"){E.attrHandle.href=function(S){return S.getAttribute("href",2)
}
}R=null
})();
if(document.querySelectorAll){(function(){var R=B,T=document.createElement("div");
T.innerHTML="<p class='TEST'></p>";
if(T.querySelectorAll&&T.querySelectorAll(".TEST").length===0){return 
}B=function(X,W,U,V){W=W||document;
if(!V&&W.nodeType===9&&!O(W)){try{return A(W.querySelectorAll(X),U)
}catch(Y){}}return R(X,W,U,V)
};
for(var S in R){B[S]=R[S]
}T=null
})()
}if(document.getElementsByClassName&&document.documentElement.getElementsByClassName){(function(){var R=document.createElement("div");
R.innerHTML="<div class='test e'></div><div class='test'></div>";
if(R.getElementsByClassName("e").length===0){return 
}R.lastChild.className="e";
if(R.getElementsByClassName("e").length===1){return 
}E.order.splice(1,0,"CLASS");
E.find.CLASS=function(S,T,U){if(typeof T.getElementsByClassName!=="undefined"&&!U){return T.getElementsByClassName(S[1])
}};
R=null
})()
}function M(S,X,W,b,Y,a){var Z=S=="previousSibling"&&!a;
for(var U=0,T=b.length;
U<T;
U++){var R=b[U];
if(R){if(Z&&R.nodeType===1){R.sizcache=W;
R.sizset=U
}R=R[S];
var V=false;
while(R){if(R.sizcache===W){V=b[R.sizset];
break
}if(R.nodeType===1&&!a){R.sizcache=W;
R.sizset=U
}if(R.nodeName===X){V=R;
break
}R=R[S]
}b[U]=V
}}}function Q(S,X,W,b,Y,a){var Z=S=="previousSibling"&&!a;
for(var U=0,T=b.length;
U<T;
U++){var R=b[U];
if(R){if(Z&&R.nodeType===1){R.sizcache=W;
R.sizset=U
}R=R[S];
var V=false;
while(R){if(R.sizcache===W){V=b[R.sizset];
break
}if(R.nodeType===1){if(!a){R.sizcache=W;
R.sizset=U
}if(typeof X!=="string"){if(R===X){V=true;
break
}}else{if(B.filter(X,[R]).length>0){V=R;
break
}}}R=R[S]
}b[U]=V
}}}var G=document.compareDocumentPosition?function(S,R){return S.compareDocumentPosition(R)&16
}:function(S,R){return S!==R&&(S.contains?S.contains(R):true)
};
var O=function(R){return R.nodeType===9&&R.documentElement.nodeName!=="HTML"||!!R.ownerDocument&&R.ownerDocument.documentElement.nodeName!=="HTML"
};
var F=function(R,Y){var U=[],V="",W,T=Y.nodeType?[Y]:Y;
while((W=E.match.PSEUDO.exec(R))){V+=W[0];
R=R.replace(E.match.PSEUDO,"")
}R=E.relative[R]?R+"*":R;
for(var X=0,S=T.length;
X<S;
X++){B(R,T[X],U)
}return B.filter(V,U)
};
window.Sizzle=B
})();
Prototype._original_property=window.Sizzle;
(function(C){var D=Prototype.Selector.extendElements;
function A(E,F){return D(C(E,F||document))
}function B(F,E){return C.matches(E,[F]).length==1
}Prototype.Selector.engine=C;
Prototype.Selector.select=A;
Prototype.Selector.match=B
})(Sizzle);
window.Sizzle=Prototype._original_property;
delete Prototype._original_property;
var Form={reset:function(A){A=$(A);
A.reset();
return A
},serializeElements:function(H,D){if(typeof D!="object"){D={hash:!!D}
}else{if(Object.isUndefined(D.hash)){D.hash=true
}}var E,G,A=false,F=D.submit,B,C;
if(D.hash){C={};
B=function(I,J,K){if(J in I){if(!Object.isArray(I[J])){I[J]=[I[J]]
}I[J].push(K)
}else{I[J]=K
}return I
}
}else{C="";
B=function(I,J,K){return I+(I?"&":"")+encodeURIComponent(J)+"="+encodeURIComponent(K)
}
}return H.inject(C,function(I,J){if(!J.disabled&&J.name){E=J.name;
G=$(J).getValue();
if(G!=null&&J.type!="file"&&(J.type!="submit"||(!A&&F!==false&&(!F||E==F)&&(A=true)))){I=B(I,E,G)
}}return I
})
}};
Form.Methods={serialize:function(B,A){return Form.serializeElements(Form.getElements(B),A)
},getElements:function(E){var F=$(E).getElementsByTagName("*"),D,A=[],C=Form.Element.Serializers;
for(var B=0;
D=F[B];
B++){A.push(D)
}return A.inject([],function(G,H){if(C[H.tagName.toLowerCase()]){G.push(Element.extend(H))
}return G
})
},getInputs:function(G,C,D){G=$(G);
var A=G.getElementsByTagName("input");
if(!C&&!D){return $A(A).map(Element.extend)
}for(var E=0,H=[],F=A.length;
E<F;
E++){var B=A[E];
if((C&&B.type!=C)||(D&&B.name!=D)){continue
}H.push(Element.extend(B))
}return H
},disable:function(A){A=$(A);
Form.getElements(A).invoke("disable");
return A
},enable:function(A){A=$(A);
Form.getElements(A).invoke("enable");
return A
},findFirstElement:function(B){var C=$(B).getElements().findAll(function(D){return"hidden"!=D.type&&!D.disabled
});
var A=C.findAll(function(D){return D.hasAttribute("tabIndex")&&D.tabIndex>=0
}).sortBy(function(D){return D.tabIndex
}).first();
return A?A:C.find(function(D){return/^(?:input|select|textarea)$/i.test(D.tagName)
})
},focusFirstElement:function(B){B=$(B);
var A=B.findFirstElement();
if(A){A.activate()
}return B
},request:function(B,A){B=$(B),A=Object.clone(A||{});
var D=A.parameters,C=B.readAttribute("action")||"";
if(C.blank()){C=window.location.href
}A.parameters=B.serialize(true);
if(D){if(Object.isString(D)){D=D.toQueryParams()
}Object.extend(A.parameters,D)
}if(B.hasAttribute("method")&&!A.method){A.method=B.method
}return new Ajax.Request(C,A)
}};
Form.Element={focus:function(A){$(A).focus();
return A
},select:function(A){$(A).select();
return A
}};
Form.Element.Methods={serialize:function(A){A=$(A);
if(!A.disabled&&A.name){var B=A.getValue();
if(B!=undefined){var C={};
C[A.name]=B;
return Object.toQueryString(C)
}}return""
},getValue:function(A){A=$(A);
var B=A.tagName.toLowerCase();
return Form.Element.Serializers[B](A)
},setValue:function(A,B){A=$(A);
var C=A.tagName.toLowerCase();
Form.Element.Serializers[C](A,B);
return A
},clear:function(A){$(A).value="";
return A
},present:function(A){return $(A).value!=""
},activate:function(A){A=$(A);
try{A.focus();
if(A.select&&(A.tagName.toLowerCase()!="input"||!(/^(?:button|reset|submit)$/i.test(A.type)))){A.select()
}}catch(B){}return A
},disable:function(A){A=$(A);
A.disabled=true;
return A
},enable:function(A){A=$(A);
A.disabled=false;
return A
}};
var Field=Form.Element;
var $F=Form.Element.Methods.getValue;
Form.Element.Serializers=(function(){function B(H,I){switch(H.type.toLowerCase()){case"checkbox":case"radio":return F(H,I);
default:return E(H,I)
}}function F(H,I){if(Object.isUndefined(I)){return H.checked?H.value:null
}else{H.checked=!!I
}}function E(H,I){if(Object.isUndefined(I)){return H.value
}else{H.value=I
}}function A(J,M){if(Object.isUndefined(M)){return(J.type==="select-one"?C:D)(J)
}var I,K,N=!Object.isArray(M);
for(var H=0,L=J.length;
H<L;
H++){I=J.options[H];
K=this.optionValue(I);
if(N){if(K==M){I.selected=true;
return 
}}else{I.selected=M.include(K)
}}}function C(I){var H=I.selectedIndex;
return H>=0?G(I.options[H]):null
}function D(K){var H,L=K.length;
if(!L){return null
}for(var J=0,H=[];
J<L;
J++){var I=K.options[J];
if(I.selected){H.push(G(I))
}}return H
}function G(H){return Element.hasAttribute(H,"value")?H.value:H.text
}return{input:B,inputSelector:F,textarea:E,select:A,selectOne:C,selectMany:D,optionValue:G,button:E}
})();
Abstract.TimedObserver=Class.create(PeriodicalExecuter,{initialize:function($super,A,B,C){$super(C,B);
this.element=$(A);
this.lastValue=this.getValue()
},execute:function(){var A=this.getValue();
if(Object.isString(this.lastValue)&&Object.isString(A)?this.lastValue!=A:String(this.lastValue)!=String(A)){this.callback(this.element,A);
this.lastValue=A
}}});
Form.Element.Observer=Class.create(Abstract.TimedObserver,{getValue:function(){return Form.Element.getValue(this.element)
}});
Form.Observer=Class.create(Abstract.TimedObserver,{getValue:function(){return Form.serialize(this.element)
}});
Abstract.EventObserver=Class.create({initialize:function(A,B){this.element=$(A);
this.callback=B;
this.lastValue=this.getValue();
if(this.element.tagName.toLowerCase()=="form"){this.registerFormCallbacks()
}else{this.registerCallback(this.element)
}},onElementEvent:function(){var A=this.getValue();
if(this.lastValue!=A){this.callback(this.element,A);
this.lastValue=A
}},registerFormCallbacks:function(){Form.getElements(this.element).each(this.registerCallback,this)
},registerCallback:function(A){if(A.type){switch(A.type.toLowerCase()){case"checkbox":case"radio":Event.observe(A,"click",this.onElementEvent.bind(this));
break;
default:Event.observe(A,"change",this.onElementEvent.bind(this));
break
}}}});
Form.Element.EventObserver=Class.create(Abstract.EventObserver,{getValue:function(){return Form.Element.getValue(this.element)
}});
Form.EventObserver=Class.create(Abstract.EventObserver,{getValue:function(){return Form.serialize(this.element)
}});
(function(){var c={KEY_BACKSPACE:8,KEY_TAB:9,KEY_RETURN:13,KEY_ESC:27,KEY_LEFT:37,KEY_UP:38,KEY_RIGHT:39,KEY_DOWN:40,KEY_DELETE:46,KEY_HOME:36,KEY_END:35,KEY_PAGEUP:33,KEY_PAGEDOWN:34,KEY_INSERT:45,cache:{}};
var F=document.documentElement;
var d="onmouseenter" in F&&"onmouseleave" in F;
var A=function(e){return false
};
if(window.attachEvent){if(window.addEventListener){A=function(e){return !(e instanceof window.Event)
}
}else{A=function(e){return true
}
}}var R;
function a(f,e){return f.which?(f.which===e+1):(f.button===e)
}var O={0:1,1:4,2:2};
function Y(f,e){return f.button===O[e]
}function b(f,e){switch(e){case 0:return f.which==1&&!f.metaKey;
case 1:return f.which==2||(f.which==1&&f.metaKey);
case 2:return f.which==3;
default:return false
}}if(window.attachEvent){if(!window.addEventListener){R=Y
}else{R=function(f,e){return A(f)?Y(f,e):a(f,e)
}
}}else{if(Prototype.Browser.WebKit){R=b
}else{R=a
}}function V(e){return R(e,0)
}function T(e){return R(e,1)
}function N(e){return R(e,2)
}function D(g){g=c.extend(g);
var f=g.target,e=g.type,h=g.currentTarget;
if(h&&h.tagName){if(e==="load"||e==="error"||(e==="click"&&h.tagName.toLowerCase()==="input"&&h.type==="radio")){f=h
}}if(f.nodeType==Node.TEXT_NODE){f=f.parentNode
}return Element.extend(f)
}function P(f,g){var e=c.element(f);
if(!g){return e
}while(e){if(Object.isElement(e)&&Prototype.Selector.match(e,g)){return Element.extend(e)
}e=e.parentNode
}}function S(e){return{x:C(e),y:B(e)}
}function C(g){var f=document.documentElement,e=document.body||{scrollLeft:0};
return g.pageX||(g.clientX+(f.scrollLeft||e.scrollLeft)-(f.clientLeft||0))
}function B(g){var f=document.documentElement,e=document.body||{scrollTop:0};
return g.pageY||(g.clientY+(f.scrollTop||e.scrollTop)-(f.clientTop||0))
}function Q(e){c.extend(e);
e.preventDefault();
e.stopPropagation();
e.stopped=true
}c.Methods={isLeftClick:V,isMiddleClick:T,isRightClick:N,element:D,findElement:P,pointer:S,pointerX:C,pointerY:B,stop:Q};
var X=Object.keys(c.Methods).inject({},function(e,f){e[f]=c.Methods[f].methodize();
return e
});
if(window.attachEvent){function I(f){var e;
switch(f.type){case"mouseover":case"mouseenter":e=f.fromElement;
break;
case"mouseout":case"mouseleave":e=f.toElement;
break;
default:return null
}return Element.extend(e)
}var U={stopPropagation:function(){this.cancelBubble=true
},preventDefault:function(){this.returnValue=false
},inspect:function(){return"[object Event]"
}};
c.extend=function(f,e){if(!f){return false
}if(!A(f)){return f
}if(f._extendedByPrototype){return f
}f._extendedByPrototype=Prototype.emptyFunction;
var g=c.pointer(f);
Object.extend(f,{target:f.srcElement||e,relatedTarget:I(f),pageX:g.x,pageY:g.y});
Object.extend(f,X);
Object.extend(f,U);
return f
}
}else{c.extend=Prototype.K
}if(window.addEventListener){c.prototype=window.Event.prototype||document.createEvent("HTMLEvents").__proto__;
Object.extend(c.prototype,X)
}function M(i,h,j){var g=Element.retrieve(i,"prototype_event_registry");
if(Object.isUndefined(g)){E.push(i);
g=Element.retrieve(i,"prototype_event_registry",$H())
}var e=g.get(h);
if(Object.isUndefined(e)){e=[];
g.set(h,e)
}if(e.pluck("handler").include(j)){return false
}var f;
if(h.include(":")){f=function(k){if(Object.isUndefined(k.eventName)){return false
}if(k.eventName!==h){return false
}c.extend(k,i);
j.call(i,k)
}
}else{if(!d&&(h==="mouseenter"||h==="mouseleave")){if(h==="mouseenter"||h==="mouseleave"){f=function(l){c.extend(l,i);
var k=l.relatedTarget;
while(k&&k!==i){try{k=k.parentNode
}catch(m){k=i
}}if(k===i){return 
}j.call(i,l)
}
}}else{f=function(k){c.extend(k,i);
j.call(i,k)
}
}}f.handler=j;
e.push(f);
return f
}function H(){for(var e=0,f=E.length;
e<f;
e++){c.stopObserving(E[e]);
E[e]=null
}}var E=[];
if(Prototype.Browser.IE){window.attachEvent("onunload",H)
}if(Prototype.Browser.WebKit){window.addEventListener("unload",Prototype.emptyFunction,false)
}var L=Prototype.K,G={mouseenter:"mouseover",mouseleave:"mouseout"};
if(!d){L=function(e){return(G[e]||e)
}
}function W(h,g,i){h=$(h);
var f=M(h,g,i);
if(!f){return h
}if(g.include(":")){if(h.addEventListener){h.addEventListener("dataavailable",f,false)
}else{h.attachEvent("ondataavailable",f);
h.attachEvent("onlosecapture",f)
}}else{var e=L(g);
if(h.addEventListener){h.addEventListener(e,f,false)
}else{h.attachEvent("on"+e,f)
}}return h
}function K(l,h,m){l=$(l);
var g=Element.retrieve(l,"prototype_event_registry");
if(!g){return l
}if(!h){g.each(function(n){var i=n.key;
K(l,i)
});
return l
}var j=g.get(h);
if(!j){return l
}if(!m){j.each(function(i){K(l,h,i.handler)
});
return l
}var k=j.length,f;
while(k--){if(j[k].handler===m){f=j[k];
break
}}if(!f){return l
}if(h.include(":")){if(l.removeEventListener){l.removeEventListener("dataavailable",f,false)
}else{l.detachEvent("ondataavailable",f);
l.detachEvent("onlosecapture",f)
}}else{var e=L(h);
if(l.removeEventListener){l.removeEventListener(e,f,false)
}else{l.detachEvent("on"+e,f)
}}g.set(h,j.without(f));
return l
}function Z(h,g,f,e){h=$(h);
if(Object.isUndefined(e)){e=true
}if(h==document&&document.createEvent&&!h.dispatchEvent){h=document.documentElement
}var i;
if(document.createEvent){i=document.createEvent("HTMLEvents");
i.initEvent("dataavailable",e,true)
}else{i=document.createEventObject();
i.eventType=e?"ondataavailable":"onlosecapture"
}i.eventName=g;
i.memo=f||{};
if(document.createEvent){h.dispatchEvent(i)
}else{h.fireEvent(i.eventType,i)
}return c.extend(i)
}c.Handler=Class.create({initialize:function(g,f,e,h){this.element=$(g);
this.eventName=f;
this.selector=e;
this.callback=h;
this.handler=this.handleEvent.bind(this)
},start:function(){c.observe(this.element,this.eventName,this.handler);
return this
},stop:function(){c.stopObserving(this.element,this.eventName,this.handler);
return this
},handleEvent:function(f){var e=c.findElement(f,this.selector);
if(e){this.callback.call(this.element,f,e)
}}});
function J(g,f,e,h){g=$(g);
if(Object.isFunction(e)&&Object.isUndefined(h)){h=e,e=null
}return new c.Handler(g,f,e,h).start()
}Object.extend(c,c.Methods);
Object.extend(c,{fire:Z,observe:W,stopObserving:K,on:J});
Element.addMethods({fire:Z,observe:W,stopObserving:K,on:J});
Object.extend(document,{fire:Z.methodize(),observe:W.methodize(),stopObserving:K.methodize(),on:J.methodize(),loaded:false});
if(window.Event){Object.extend(window.Event,c)
}else{window.Event=c
}})();
(function(){var D;
function A(){if(document.loaded){return 
}if(D){window.clearTimeout(D)
}document.loaded=true;
document.fire("dom:loaded")
}function C(){if(document.readyState==="complete"){document.stopObserving("readystatechange",C);
A()
}}function B(){try{document.documentElement.doScroll("left")
}catch(E){D=B.defer();
return 
}A()
}if(document.addEventListener){document.addEventListener("DOMContentLoaded",A,false)
}else{document.observe("readystatechange",C);
if(window==top){D=B.defer()
}}Event.observe(window,"load",A)
})();
Element.addMethods();
Hash.toQueryString=Object.toQueryString;
var Toggle={display:Element.toggle};
Element.Methods.childOf=Element.Methods.descendantOf;
var Insertion={Before:function(A,B){return Element.insert(A,{before:B})
},Top:function(A,B){return Element.insert(A,{top:B})
},Bottom:function(A,B){return Element.insert(A,{bottom:B})
},After:function(A,B){return Element.insert(A,{after:B})
}};
var $continue=new Error('"throw $continue" is deprecated, use "return" instead');
var Position={includeScrollOffsets:false,prepare:function(){this.deltaX=window.pageXOffset||document.documentElement.scrollLeft||document.body.scrollLeft||0;
this.deltaY=window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0
},within:function(B,A,C){if(this.includeScrollOffsets){return this.withinIncludingScrolloffsets(B,A,C)
}this.xcomp=A;
this.ycomp=C;
this.offset=Element.cumulativeOffset(B);
return(C>=this.offset[1]&&C<this.offset[1]+B.offsetHeight&&A>=this.offset[0]&&A<this.offset[0]+B.offsetWidth)
},withinIncludingScrolloffsets:function(B,A,D){var C=Element.cumulativeScrollOffset(B);
this.xcomp=A+C[0]-this.deltaX;
this.ycomp=D+C[1]-this.deltaY;
this.offset=Element.cumulativeOffset(B);
return(this.ycomp>=this.offset[1]&&this.ycomp<this.offset[1]+B.offsetHeight&&this.xcomp>=this.offset[0]&&this.xcomp<this.offset[0]+B.offsetWidth)
},overlap:function(B,A){if(!B){return 0
}if(B=="vertical"){return((this.offset[1]+A.offsetHeight)-this.ycomp)/A.offsetHeight
}if(B=="horizontal"){return((this.offset[0]+A.offsetWidth)-this.xcomp)/A.offsetWidth
}},cumulativeOffset:Element.Methods.cumulativeOffset,positionedOffset:Element.Methods.positionedOffset,absolutize:function(A){Position.prepare();
return Element.absolutize(A)
},relativize:function(A){Position.prepare();
return Element.relativize(A)
},realOffset:Element.Methods.cumulativeScrollOffset,offsetParent:Element.Methods.getOffsetParent,page:Element.Methods.viewportOffset,clone:function(B,C,A){A=A||{};
return Element.clonePosition(C,B,A)
}};
if(!document.getElementsByClassName){document.getElementsByClassName=function(B){function A(C){return C.blank()?null:"[contains(concat(' ', @class, ' '), ' "+C+" ')]"
}B.getElementsByClassName=Prototype.BrowserFeatures.XPath?function(C,E){E=E.toString().strip();
var D=/\s/.test(E)?$w(E).map(A).join(""):A(E);
return D?document._getElementsByXPath(".//*"+D,C):[]
}:function(E,F){F=F.toString().strip();
var G=[],H=(/\s/.test(F)?$w(F):null);
if(!H&&!F){return G
}var C=$(E).getElementsByTagName("*");
F=" "+F+" ";
for(var D=0,J,I;
J=C[D];
D++){if(J.className&&(I=" "+J.className+" ")&&(I.include(F)||(H&&H.all(function(K){return !K.toString().blank()&&I.include(" "+K+" ")
})))){G.push(Element.extend(J))
}}return G
};
return function(D,C){return $(C||document.body).getElementsByClassName(D)
}
}(Element.Methods)
}Element.ClassNames=Class.create();
Element.ClassNames.prototype={initialize:function(A){this.element=$(A)
},_each:function(A){this.element.className.split(/\s+/).select(function(B){return B.length>0
})._each(A)
},set:function(A){this.element.className=A
},add:function(A){if(this.include(A)){return 
}this.set($A(this).concat(A).join(" "))
},remove:function(A){if(!this.include(A)){return 
}this.set($A(this).without(A).join(" "))
},toString:function(){return $A(this).join(" ")
}};
Object.extend(Element.ClassNames.prototype,Enumerable);
(function(){window.Selector=Class.create({initialize:function(A){this.expression=A.strip()
},findElements:function(A){return Prototype.Selector.select(this.expression,A)
},match:function(A){return Prototype.Selector.match(A,this.expression)
},toString:function(){return this.expression
},inspect:function(){return"#<Selector: "+this.expression+">"
}});
Object.extend(Selector,{matchElements:function(F,G){var A=Prototype.Selector.match,D=[];
for(var C=0,E=F.length;
C<E;
C++){var B=F[C];
if(A(B,G)){D.push(Element.extend(B))
}}return D
},findElement:function(F,G,B){B=B||0;
var A=0,D;
for(var C=0,E=F.length;
C<E;
C++){D=F[C];
if(Prototype.Selector.match(D,G)&&B===A++){return Element.extend(D)
}}},findChildElements:function(B,C){var A=C.toArray().join(", ");
return Prototype.Selector.select(A,B||document)
}})
})();var Builder={NODEMAP:{AREA:"map",CAPTION:"table",COL:"table",COLGROUP:"table",LEGEND:"fieldset",OPTGROUP:"select",OPTION:"select",PARAM:"object",TBODY:"table",TD:"table",TFOOT:"table",TH:"table",THEAD:"table",TR:"table"},node:function(A){A=A.toUpperCase();
var F=this.NODEMAP[A]||"div";
var B=document.createElement(F);
try{B.innerHTML="<"+A+"></"+A+">"
}catch(E){}var D=B.firstChild||null;
if(D&&(D.tagName.toUpperCase()!=A)){D=D.getElementsByTagName(A)[0]
}if(!D){D=document.createElement(A)
}if(!D){return 
}if(arguments[1]){if(this._isStringOrNumber(arguments[1])||(arguments[1] instanceof Array)||arguments[1].tagName){this._children(D,arguments[1])
}else{var C=this._attributes(arguments[1]);
if(C.length){try{B.innerHTML="<"+A+" "+C+"></"+A+">"
}catch(E){}D=B.firstChild||null;
if(!D){D=document.createElement(A);
for(attr in arguments[1]){D[attr=="class"?"className":attr]=arguments[1][attr]
}}if(D.tagName.toUpperCase()!=A){D=B.getElementsByTagName(A)[0]
}}}}if(arguments[2]){this._children(D,arguments[2])
}return $(D)
},_text:function(A){return document.createTextNode(A)
},ATTR_MAP:{className:"class",htmlFor:"for"},_attributes:function(A){var B=[];
for(attribute in A){B.push((attribute in this.ATTR_MAP?this.ATTR_MAP[attribute]:attribute)+'="'+A[attribute].toString().escapeHTML().gsub(/"/,"&quot;")+'"')
}return B.join(" ")
},_children:function(B,A){if(A.tagName){B.appendChild(A);
return 
}if(typeof A=="object"){A.flatten().each(function(C){if(typeof C=="object"){B.appendChild(C)
}else{if(Builder._isStringOrNumber(C)){B.appendChild(Builder._text(C))
}}})
}else{if(Builder._isStringOrNumber(A)){B.appendChild(Builder._text(A))
}}},_isStringOrNumber:function(A){return(typeof A=="string"||typeof A=="number")
},build:function(B){var A=this.node("div");
$(A).update(B.strip());
return A.down()
},dump:function(B){if(typeof B!="object"&&typeof B!="function"){B=window
}var A=("A ABBR ACRONYM ADDRESS APPLET AREA B BASE BASEFONT BDO BIG BLOCKQUOTE BODY BR BUTTON CAPTION CENTER CITE CODE COL COLGROUP DD DEL DFN DIR DIV DL DT EM FIELDSET FONT FORM FRAME FRAMESET H1 H2 H3 H4 H5 H6 HEAD HR HTML I IFRAME IMG INPUT INS ISINDEX KBD LABEL LEGEND LI LINK MAP MENU META NOFRAMES NOSCRIPT OBJECT OL OPTGROUP OPTION P PARAM PRE Q S SAMP SCRIPT SELECT SMALL SPAN STRIKE STRONG STYLE SUB SUP TABLE TBODY TD TEXTAREA TFOOT TH THEAD TITLE TR TT U UL VAR").split(/\s+/);
A.each(function(C){B[C]=function(){return Builder.node.apply(Builder,[C].concat($A(arguments)))
}
})
}};
String.prototype.parseColor=function(){var A="#";
if(this.slice(0,4)=="rgb("){var C=this.slice(4,this.length-1).split(",");
var B=0;
do{A+=parseInt(C[B]).toColorPart()
}while(++B<3)
}else{if(this.slice(0,1)=="#"){if(this.length==4){for(var B=1;
B<4;
B++){A+=(this.charAt(B)+this.charAt(B)).toLowerCase()
}}if(this.length==7){A=this.toLowerCase()
}}}return(A.length==7?A:(arguments[0]||this))
};
Element.collectTextNodes=function(A){return $A($(A).childNodes).collect(function(B){return(B.nodeType==3?B.nodeValue:(B.hasChildNodes()?Element.collectTextNodes(B):""))
}).flatten().join("")
};
Element.collectTextNodesIgnoreClass=function(A,B){return $A($(A).childNodes).collect(function(C){return(C.nodeType==3?C.nodeValue:((C.hasChildNodes()&&!Element.hasClassName(C,B))?Element.collectTextNodesIgnoreClass(C,B):""))
}).flatten().join("")
};
Element.setContentZoom=function(A,B){A=$(A);
A.setStyle({fontSize:(B/100)+"em"});
if(Prototype.Browser.WebKit){window.scrollBy(0,0)
}return A
};
Element.getInlineOpacity=function(A){return $(A).style.opacity||""
};
Element.forceRerendering=function(A){try{A=$(A);
var C=document.createTextNode(" ");
A.appendChild(C);
A.removeChild(C)
}catch(B){}};
var Effect={_elementDoesNotExistError:{name:"ElementDoesNotExistError",message:"The specified DOM element does not exist, but is required for this effect to operate"},Transitions:{linear:Prototype.K,sinoidal:function(A){return(-Math.cos(A*Math.PI)/2)+0.5
},reverse:function(A){return 1-A
},flicker:function(A){var A=((-Math.cos(A*Math.PI)/4)+0.75)+Math.random()/4;
return A>1?1:A
},wobble:function(A){return(-Math.cos(A*Math.PI*(9*A))/2)+0.5
},pulse:function(B,A){return(-Math.cos((B*((A||5)-0.5)*2)*Math.PI)/2)+0.5
},spring:function(A){return 1-(Math.cos(A*4.5*Math.PI)*Math.exp(-A*6))
},none:function(A){return 0
},full:function(A){return 1
}},DefaultOptions:{duration:1,fps:100,sync:false,from:0,to:1,delay:0,queue:"parallel"},tagifyText:function(A){var B="position:relative";
if(Prototype.Browser.IE){B+=";zoom:1"
}A=$(A);
$A(A.childNodes).each(function(C){if(C.nodeType==3){C.nodeValue.toArray().each(function(D){A.insertBefore(new Element("span",{style:B}).update(D==" "?String.fromCharCode(160):D),C)
});
Element.remove(C)
}})
},multiple:function(B,C){var E;
if(((typeof B=="object")||Object.isFunction(B))&&(B.length)){E=B
}else{E=$(B).childNodes
}var A=Object.extend({speed:0.1,delay:0},arguments[2]||{});
var D=A.delay;
$A(E).each(function(G,F){new C(G,Object.extend(A,{delay:F*A.speed+D}))
})
},PAIRS:{slide:["SlideDown","SlideUp"],blind:["BlindDown","BlindUp"],appear:["Appear","Fade"]},toggle:function(B,C,A){B=$(B);
C=(C||"appear").toLowerCase();
return Effect[Effect.PAIRS[C][B.visible()?1:0]](B,Object.extend({queue:{position:"end",scope:(B.id||"global"),limit:1}},A||{}))
}};
Effect.DefaultOptions.transition=Effect.Transitions.sinoidal;
Effect.ScopedQueue=Class.create(Enumerable,{initialize:function(){this.effects=[];
this.interval=null
},_each:function(A){this.effects._each(A)
},add:function(B){var C=new Date().getTime();
var A=Object.isString(B.options.queue)?B.options.queue:B.options.queue.position;
switch(A){case"front":this.effects.findAll(function(D){return D.state=="idle"
}).each(function(D){D.startOn+=B.finishOn;
D.finishOn+=B.finishOn
});
break;
case"with-last":C=this.effects.pluck("startOn").max()||C;
break;
case"end":C=this.effects.pluck("finishOn").max()||C;
break
}B.startOn+=C;
B.finishOn+=C;
if(!B.options.queue.limit||(this.effects.length<B.options.queue.limit)){this.effects.push(B)
}if(!this.interval){this.interval=setInterval(this.loop.bind(this),15)
}},remove:function(A){this.effects=this.effects.reject(function(B){return B==A
});
if(this.effects.length==0){clearInterval(this.interval);
this.interval=null
}},loop:function(){var C=new Date().getTime();
for(var B=0,A=this.effects.length;
B<A;
B++){this.effects[B]&&this.effects[B].loop(C)
}}});
Effect.Queues={instances:$H(),get:function(A){if(!Object.isString(A)){return A
}return this.instances.get(A)||this.instances.set(A,new Effect.ScopedQueue())
}};
Effect.Queue=Effect.Queues.get("global");
Effect.Base=Class.create({position:null,start:function(A){if(A&&A.transition===false){A.transition=Effect.Transitions.linear
}this.options=Object.extend(Object.extend({},Effect.DefaultOptions),A||{});
this.currentFrame=0;
this.state="idle";
this.startOn=this.options.delay*1000;
this.finishOn=this.startOn+(this.options.duration*1000);
this.fromToDelta=this.options.to-this.options.from;
this.totalTime=this.finishOn-this.startOn;
this.totalFrames=this.options.fps*this.options.duration;
this.render=(function(){function B(D,C){if(D.options[C+"Internal"]){D.options[C+"Internal"](D)
}if(D.options[C]){D.options[C](D)
}}return function(C){if(this.state==="idle"){this.state="running";
B(this,"beforeSetup");
if(this.setup){this.setup()
}B(this,"afterSetup")
}if(this.state==="running"){C=(this.options.transition(C)*this.fromToDelta)+this.options.from;
this.position=C;
B(this,"beforeUpdate");
if(this.update){this.update(C)
}B(this,"afterUpdate")
}}
})();
this.event("beforeStart");
if(!this.options.sync){Effect.Queues.get(Object.isString(this.options.queue)?"global":this.options.queue.scope).add(this)
}},loop:function(C){if(C>=this.startOn){if(C>=this.finishOn){this.render(1);
this.cancel();
this.event("beforeFinish");
if(this.finish){this.finish()
}this.event("afterFinish");
return 
}var B=(C-this.startOn)/this.totalTime,A=(B*this.totalFrames).round();
if(A>this.currentFrame){this.render(B);
this.currentFrame=A
}}},cancel:function(){if(!this.options.sync){Effect.Queues.get(Object.isString(this.options.queue)?"global":this.options.queue.scope).remove(this)
}this.state="finished"
},event:function(A){if(this.options[A+"Internal"]){this.options[A+"Internal"](this)
}if(this.options[A]){this.options[A](this)
}},inspect:function(){var A=$H();
for(property in this){if(!Object.isFunction(this[property])){A.set(property,this[property])
}}return"#<Effect:"+A.inspect()+",options:"+$H(this.options).inspect()+">"
}});
Effect.Parallel=Class.create(Effect.Base,{initialize:function(A){this.effects=A||[];
this.start(arguments[1])
},update:function(A){this.effects.invoke("render",A)
},finish:function(A){this.effects.each(function(B){B.render(1);
B.cancel();
B.event("beforeFinish");
if(B.finish){B.finish(A)
}B.event("afterFinish")
})
}});
Effect.Tween=Class.create(Effect.Base,{initialize:function(C,F,E){C=Object.isString(C)?$(C):C;
var B=$A(arguments),D=B.last(),A=B.length==5?B[3]:null;
this.method=Object.isFunction(D)?D.bind(C):Object.isFunction(C[D])?C[D].bind(C):function(G){C[D]=G
};
this.start(Object.extend({from:F,to:E},A||{}))
},update:function(A){this.method(A)
}});
Effect.Event=Class.create(Effect.Base,{initialize:function(){this.start(Object.extend({duration:0},arguments[0]||{}))
},update:Prototype.emptyFunction});
Effect.Opacity=Class.create(Effect.Base,{initialize:function(B){this.element=$(B);
if(!this.element){throw (Effect._elementDoesNotExistError)
}if(Prototype.Browser.IE&&(!this.element.currentStyle.hasLayout)){this.element.setStyle({zoom:1})
}var A=Object.extend({from:this.element.getOpacity()||0,to:1},arguments[1]||{});
this.start(A)
},update:function(A){this.element.setOpacity(A)
}});
Effect.Move=Class.create(Effect.Base,{initialize:function(B){this.element=$(B);
if(!this.element){throw (Effect._elementDoesNotExistError)
}var A=Object.extend({x:0,y:0,mode:"relative"},arguments[1]||{});
this.start(A)
},setup:function(){this.element.makePositioned();
this.originalLeft=parseFloat(this.element.getStyle("left")||"0");
this.originalTop=parseFloat(this.element.getStyle("top")||"0");
if(this.options.mode=="absolute"){this.options.x=this.options.x-this.originalLeft;
this.options.y=this.options.y-this.originalTop
}},update:function(A){this.element.setStyle({left:(this.options.x*A+this.originalLeft).round()+"px",top:(this.options.y*A+this.originalTop).round()+"px"})
}});
Effect.MoveBy=function(B,A,C){return new Effect.Move(B,Object.extend({x:C,y:A},arguments[3]||{}))
};
Effect.Scale=Class.create(Effect.Base,{initialize:function(B,C){this.element=$(B);
if(!this.element){throw (Effect._elementDoesNotExistError)
}var A=Object.extend({scaleX:true,scaleY:true,scaleContent:true,scaleFromCenter:false,scaleMode:"box",scaleFrom:100,scaleTo:C},arguments[2]||{});
this.start(A)
},setup:function(){this.restoreAfterFinish=this.options.restoreAfterFinish||false;
this.elementPositioning=this.element.getStyle("position");
this.originalStyle={};
["top","left","width","height","fontSize"].each(function(B){this.originalStyle[B]=this.element.style[B]
}.bind(this));
this.originalTop=this.element.offsetTop;
this.originalLeft=this.element.offsetLeft;
var A=this.element.getStyle("font-size")||"100%";
["em","px","%","pt"].each(function(B){if(A.indexOf(B)>0){this.fontSize=parseFloat(A);
this.fontSizeType=B
}}.bind(this));
this.factor=(this.options.scaleTo-this.options.scaleFrom)/100;
this.dims=null;
if(this.options.scaleMode=="box"){this.dims=[this.element.offsetHeight,this.element.offsetWidth]
}if(/^content/.test(this.options.scaleMode)){this.dims=[this.element.scrollHeight,this.element.scrollWidth]
}if(!this.dims){this.dims=[this.options.scaleMode.originalHeight,this.options.scaleMode.originalWidth]
}},update:function(A){var B=(this.options.scaleFrom/100)+(this.factor*A);
if(this.options.scaleContent&&this.fontSize){this.element.setStyle({fontSize:this.fontSize*B+this.fontSizeType})
}this.setDimensions(this.dims[0]*B,this.dims[1]*B)
},finish:function(A){if(this.restoreAfterFinish){this.element.setStyle(this.originalStyle)
}},setDimensions:function(A,D){var E={};
if(this.options.scaleX){E.width=D.round()+"px"
}if(this.options.scaleY){E.height=A.round()+"px"
}if(this.options.scaleFromCenter){var C=(A-this.dims[0])/2;
var B=(D-this.dims[1])/2;
if(this.elementPositioning=="absolute"){if(this.options.scaleY){E.top=this.originalTop-C+"px"
}if(this.options.scaleX){E.left=this.originalLeft-B+"px"
}}else{if(this.options.scaleY){E.top=-C+"px"
}if(this.options.scaleX){E.left=-B+"px"
}}}this.element.setStyle(E)
}});
Effect.Highlight=Class.create(Effect.Base,{initialize:function(B){this.element=$(B);
if(!this.element){throw (Effect._elementDoesNotExistError)
}var A=Object.extend({startcolor:"#ffff99"},arguments[1]||{});
this.start(A)
},setup:function(){if(this.element.getStyle("display")=="none"){this.cancel();
return 
}this.oldStyle={};
if(!this.options.keepBackgroundImage){this.oldStyle.backgroundImage=this.element.getStyle("background-image");
this.element.setStyle({backgroundImage:"none"})
}if(!this.options.endcolor){this.options.endcolor=this.element.getStyle("background-color").parseColor("#ffffff")
}if(!this.options.restorecolor){this.options.restorecolor=this.element.getStyle("background-color")
}this._base=$R(0,2).map(function(A){return parseInt(this.options.startcolor.slice(A*2+1,A*2+3),16)
}.bind(this));
this._delta=$R(0,2).map(function(A){return parseInt(this.options.endcolor.slice(A*2+1,A*2+3),16)-this._base[A]
}.bind(this))
},update:function(A){this.element.setStyle({backgroundColor:$R(0,2).inject("#",function(B,C,D){return B+((this._base[D]+(this._delta[D]*A)).round().toColorPart())
}.bind(this))})
},finish:function(){this.element.setStyle(Object.extend(this.oldStyle,{backgroundColor:this.options.restorecolor}))
}});
Effect.ScrollTo=function(C){var B=arguments[1]||{},A=document.viewport.getScrollOffsets(),D=$(C).cumulativeOffset();
if(B.offset){D[1]+=B.offset
}return new Effect.Tween(null,A.top,D[1],B,function(E){scrollTo(A.left,E.round())
})
};
Effect.Fade=function(C){C=$(C);
var A=C.getInlineOpacity();
var B=Object.extend({from:C.getOpacity()||1,to:0,afterFinishInternal:function(D){if(D.options.to!=0){return 
}D.element.hide().setStyle({opacity:A})
}},arguments[1]||{});
return new Effect.Opacity(C,B)
};
Effect.Appear=function(B){B=$(B);
var A=Object.extend({from:(B.getStyle("display")=="none"?0:B.getOpacity()||0),to:1,afterFinishInternal:function(C){C.element.forceRerendering()
},beforeSetup:function(C){C.element.setOpacity(C.options.from).show()
}},arguments[1]||{});
return new Effect.Opacity(B,A)
};
Effect.Puff=function(B){B=$(B);
var A={opacity:B.getInlineOpacity(),position:B.getStyle("position"),top:B.style.top,left:B.style.left,width:B.style.width,height:B.style.height};
return new Effect.Parallel([new Effect.Scale(B,200,{sync:true,scaleFromCenter:true,scaleContent:true,restoreAfterFinish:true}),new Effect.Opacity(B,{sync:true,to:0})],Object.extend({duration:1,beforeSetupInternal:function(C){Position.absolutize(C.effects[0].element)
},afterFinishInternal:function(C){C.effects[0].element.hide().setStyle(A)
}},arguments[1]||{}))
};
Effect.BlindUp=function(A){A=$(A);
A.makeClipping();
return new Effect.Scale(A,0,Object.extend({scaleContent:false,scaleX:false,restoreAfterFinish:true,afterFinishInternal:function(B){B.element.hide().undoClipping()
}},arguments[1]||{}))
};
Effect.BlindDown=function(B){B=$(B);
var A=B.getDimensions();
return new Effect.Scale(B,100,Object.extend({scaleContent:false,scaleX:false,scaleFrom:0,scaleMode:{originalHeight:A.height,originalWidth:A.width},restoreAfterFinish:true,afterSetup:function(C){C.element.makeClipping().setStyle({height:"0px"}).show()
},afterFinishInternal:function(C){C.element.undoClipping()
}},arguments[1]||{}))
};
Effect.SwitchOff=function(B){B=$(B);
var A=B.getInlineOpacity();
return new Effect.Appear(B,Object.extend({duration:0.4,from:0,transition:Effect.Transitions.flicker,afterFinishInternal:function(C){new Effect.Scale(C.element,1,{duration:0.3,scaleFromCenter:true,scaleX:false,scaleContent:false,restoreAfterFinish:true,beforeSetup:function(D){D.element.makePositioned().makeClipping()
},afterFinishInternal:function(D){D.element.hide().undoClipping().undoPositioned().setStyle({opacity:A})
}})
}},arguments[1]||{}))
};
Effect.DropOut=function(B){B=$(B);
var A={top:B.getStyle("top"),left:B.getStyle("left"),opacity:B.getInlineOpacity()};
return new Effect.Parallel([new Effect.Move(B,{x:0,y:100,sync:true}),new Effect.Opacity(B,{sync:true,to:0})],Object.extend({duration:0.5,beforeSetup:function(C){C.effects[0].element.makePositioned()
},afterFinishInternal:function(C){C.effects[0].element.hide().undoPositioned().setStyle(A)
}},arguments[1]||{}))
};
Effect.Shake=function(D){D=$(D);
var B=Object.extend({distance:20,duration:0.5},arguments[1]||{});
var E=parseFloat(B.distance);
var C=parseFloat(B.duration)/10;
var A={top:D.getStyle("top"),left:D.getStyle("left")};
return new Effect.Move(D,{x:E,y:0,duration:C,afterFinishInternal:function(F){new Effect.Move(F.element,{x:-E*2,y:0,duration:C*2,afterFinishInternal:function(G){new Effect.Move(G.element,{x:E*2,y:0,duration:C*2,afterFinishInternal:function(H){new Effect.Move(H.element,{x:-E*2,y:0,duration:C*2,afterFinishInternal:function(I){new Effect.Move(I.element,{x:E*2,y:0,duration:C*2,afterFinishInternal:function(J){new Effect.Move(J.element,{x:-E,y:0,duration:C,afterFinishInternal:function(K){K.element.undoPositioned().setStyle(A)
}})
}})
}})
}})
}})
}})
};
Effect.SlideDown=function(C){C=$(C).cleanWhitespace();
var A=C.down().getStyle("bottom");
var B=C.getDimensions();
return new Effect.Scale(C,100,Object.extend({scaleContent:false,scaleX:false,scaleFrom:window.opera?0:1,scaleMode:{originalHeight:B.height,originalWidth:B.width},restoreAfterFinish:true,afterSetup:function(D){D.element.makePositioned();
D.element.down().makePositioned();
if(window.opera){D.element.setStyle({top:""})
}D.element.makeClipping().setStyle({height:"0px"}).show()
},afterUpdateInternal:function(D){D.element.down().setStyle({bottom:(D.dims[0]-D.element.clientHeight)+"px"})
},afterFinishInternal:function(D){D.element.undoClipping().undoPositioned();
D.element.down().undoPositioned().setStyle({bottom:A})
}},arguments[1]||{}))
};
Effect.SlideUp=function(C){C=$(C).cleanWhitespace();
var A=C.down().getStyle("bottom");
var B=C.getDimensions();
return new Effect.Scale(C,window.opera?0:1,Object.extend({scaleContent:false,scaleX:false,scaleMode:"box",scaleFrom:100,scaleMode:{originalHeight:B.height,originalWidth:B.width},restoreAfterFinish:true,afterSetup:function(D){D.element.makePositioned();
D.element.down().makePositioned();
if(window.opera){D.element.setStyle({top:""})
}D.element.makeClipping().show()
},afterUpdateInternal:function(D){D.element.down().setStyle({bottom:(D.dims[0]-D.element.clientHeight)+"px"})
},afterFinishInternal:function(D){D.element.hide().undoClipping().undoPositioned();
D.element.down().undoPositioned().setStyle({bottom:A})
}},arguments[1]||{}))
};
Effect.Squish=function(A){return new Effect.Scale(A,window.opera?1:0,{restoreAfterFinish:true,beforeSetup:function(B){B.element.makeClipping()
},afterFinishInternal:function(B){B.element.hide().undoClipping()
}})
};
Effect.Grow=function(C){C=$(C);
var B=Object.extend({direction:"center",moveTransition:Effect.Transitions.sinoidal,scaleTransition:Effect.Transitions.sinoidal,opacityTransition:Effect.Transitions.full},arguments[1]||{});
var A={top:C.style.top,left:C.style.left,height:C.style.height,width:C.style.width,opacity:C.getInlineOpacity()};
var G=C.getDimensions();
var H,F;
var E,D;
switch(B.direction){case"top-left":H=F=E=D=0;
break;
case"top-right":H=G.width;
F=D=0;
E=-G.width;
break;
case"bottom-left":H=E=0;
F=G.height;
D=-G.height;
break;
case"bottom-right":H=G.width;
F=G.height;
E=-G.width;
D=-G.height;
break;
case"center":H=G.width/2;
F=G.height/2;
E=-G.width/2;
D=-G.height/2;
break
}return new Effect.Move(C,{x:H,y:F,duration:0.01,beforeSetup:function(I){I.element.hide().makeClipping().makePositioned()
},afterFinishInternal:function(I){new Effect.Parallel([new Effect.Opacity(I.element,{sync:true,to:1,from:0,transition:B.opacityTransition}),new Effect.Move(I.element,{x:E,y:D,sync:true,transition:B.moveTransition}),new Effect.Scale(I.element,100,{scaleMode:{originalHeight:G.height,originalWidth:G.width},sync:true,scaleFrom:window.opera?1:0,transition:B.scaleTransition,restoreAfterFinish:true})],Object.extend({beforeSetup:function(J){J.effects[0].element.setStyle({height:"0px"}).show()
},afterFinishInternal:function(J){J.effects[0].element.undoClipping().undoPositioned().setStyle(A)
}},B))
}})
};
Effect.Shrink=function(C){C=$(C);
var B=Object.extend({direction:"center",moveTransition:Effect.Transitions.sinoidal,scaleTransition:Effect.Transitions.sinoidal,opacityTransition:Effect.Transitions.none},arguments[1]||{});
var A={top:C.style.top,left:C.style.left,height:C.style.height,width:C.style.width,opacity:C.getInlineOpacity()};
var F=C.getDimensions();
var E,D;
switch(B.direction){case"top-left":E=D=0;
break;
case"top-right":E=F.width;
D=0;
break;
case"bottom-left":E=0;
D=F.height;
break;
case"bottom-right":E=F.width;
D=F.height;
break;
case"center":E=F.width/2;
D=F.height/2;
break
}return new Effect.Parallel([new Effect.Opacity(C,{sync:true,to:0,from:1,transition:B.opacityTransition}),new Effect.Scale(C,window.opera?1:0,{sync:true,transition:B.scaleTransition,restoreAfterFinish:true}),new Effect.Move(C,{x:E,y:D,sync:true,transition:B.moveTransition})],Object.extend({beforeStartInternal:function(G){G.effects[0].element.makePositioned().makeClipping()
},afterFinishInternal:function(G){G.effects[0].element.hide().undoClipping().undoPositioned().setStyle(A)
}},B))
};
Effect.Pulsate=function(C){C=$(C);
var B=arguments[1]||{},A=C.getInlineOpacity(),E=B.transition||Effect.Transitions.linear,D=function(F){return 1-E((-Math.cos((F*(B.pulses||5)*2)*Math.PI)/2)+0.5)
};
return new Effect.Opacity(C,Object.extend(Object.extend({duration:2,from:0,afterFinishInternal:function(F){F.element.setStyle({opacity:A})
}},B),{transition:D}))
};
Effect.Fold=function(B){B=$(B);
var A={top:B.style.top,left:B.style.left,width:B.style.width,height:B.style.height};
B.makeClipping();
return new Effect.Scale(B,5,Object.extend({scaleContent:false,scaleX:false,afterFinishInternal:function(C){new Effect.Scale(B,1,{scaleContent:false,scaleY:false,afterFinishInternal:function(D){D.element.hide().undoClipping().setStyle(A)
}})
}},arguments[1]||{}))
};
Effect.Morph=Class.create(Effect.Base,{initialize:function(C){this.element=$(C);
if(!this.element){throw (Effect._elementDoesNotExistError)
}var A=Object.extend({style:{}},arguments[1]||{});
if(!Object.isString(A.style)){this.style=$H(A.style)
}else{if(A.style.include(":")){this.style=A.style.parseStyle()
}else{this.element.addClassName(A.style);
this.style=$H(this.element.getStyles());
this.element.removeClassName(A.style);
var B=this.element.getStyles();
this.style=this.style.reject(function(D){return D.value==B[D.key]
});
A.afterFinishInternal=function(D){D.element.addClassName(D.options.style);
D.transforms.each(function(E){D.element.style[E.style]=""
})
}
}}this.start(A)
},setup:function(){function A(B){if(!B||["rgba(0, 0, 0, 0)","transparent"].include(B)){B="#ffffff"
}B=B.parseColor();
return $R(0,2).map(function(C){return parseInt(B.slice(C*2+1,C*2+3),16)
})
}this.transforms=this.style.map(function(G){var F=G[0],E=G[1],D=null;
if(E.parseColor("#zzzzzz")!="#zzzzzz"){E=E.parseColor();
D="color"
}else{if(F=="opacity"){E=parseFloat(E);
if(Prototype.Browser.IE&&(!this.element.currentStyle.hasLayout)){this.element.setStyle({zoom:1})
}}else{if(Element.CSS_LENGTH.test(E)){var C=E.match(/^([\+\-]?[0-9\.]+)(.*)$/);
E=parseFloat(C[1]);
D=(C.length==3)?C[2]:null
}}}var B=this.element.getStyle(F);
return{style:F.camelize(),originalValue:D=="color"?A(B):parseFloat(B||0),targetValue:D=="color"?A(E):E,unit:D}
}.bind(this)).reject(function(B){return((B.originalValue==B.targetValue)||(B.unit!="color"&&(isNaN(B.originalValue)||isNaN(B.targetValue))))
})
},update:function(A){var D={},B,C=this.transforms.length;
while(C--){D[(B=this.transforms[C]).style]=B.unit=="color"?"#"+(Math.round(B.originalValue[0]+(B.targetValue[0]-B.originalValue[0])*A)).toColorPart()+(Math.round(B.originalValue[1]+(B.targetValue[1]-B.originalValue[1])*A)).toColorPart()+(Math.round(B.originalValue[2]+(B.targetValue[2]-B.originalValue[2])*A)).toColorPart():(B.originalValue+(B.targetValue-B.originalValue)*A).toFixed(3)+(B.unit===null?"":B.unit)
}this.element.setStyle(D,true)
}});
Effect.Transform=Class.create({initialize:function(A){this.tracks=[];
this.options=arguments[1]||{};
this.addTracks(A)
},addTracks:function(A){A.each(function(B){B=$H(B);
var C=B.values().first();
this.tracks.push($H({ids:B.keys().first(),effect:Effect.Morph,options:{style:C}}))
}.bind(this));
return this
},play:function(){return new Effect.Parallel(this.tracks.map(function(A){var D=A.get("ids"),C=A.get("effect"),B=A.get("options");
var E=[$(D)||$$(D)].flatten();
return E.map(function(F){return new C(F,Object.extend({sync:true},B))
})
}).flatten(),this.options)
}});
Element.CSS_PROPERTIES=$w("backgroundColor backgroundPosition borderBottomColor borderBottomStyle borderBottomWidth borderLeftColor borderLeftStyle borderLeftWidth borderRightColor borderRightStyle borderRightWidth borderSpacing borderTopColor borderTopStyle borderTopWidth bottom clip color fontSize fontWeight height left letterSpacing lineHeight marginBottom marginLeft marginRight marginTop markerOffset maxHeight maxWidth minHeight minWidth opacity outlineColor outlineOffset outlineWidth paddingBottom paddingLeft paddingRight paddingTop right textIndent top width wordSpacing zIndex");
Element.CSS_LENGTH=/^(([\+\-]?[0-9\.]+)(em|ex|px|in|cm|mm|pt|pc|\%))|0$/;
String.__parseStyleElement=document.createElement("div");
String.prototype.parseStyle=function(){var B,A=$H();
if(Prototype.Browser.WebKit){B=new Element("div",{style:this}).style
}else{String.__parseStyleElement.innerHTML='<div style="'+this+'"></div>';
B=String.__parseStyleElement.childNodes[0].style
}Element.CSS_PROPERTIES.each(function(C){if(B[C]){A.set(C,B[C])
}});
if(Prototype.Browser.IE&&this.include("opacity")){A.set("opacity",this.match(/opacity:\s*((?:0|1)?(?:\.\d*)?)/)[1])
}return A
};
if(document.defaultView&&document.defaultView.getComputedStyle){Element.getStyles=function(B){var A=document.defaultView.getComputedStyle($(B),null);
return Element.CSS_PROPERTIES.inject({},function(C,D){C[D]=A[D];
return C
})
}
}else{Element.getStyles=function(B){B=$(B);
var A=B.currentStyle,C;
C=Element.CSS_PROPERTIES.inject({},function(D,E){D[E]=A[E];
return D
});
if(!C.opacity){C.opacity=B.getOpacity()
}return C
}
}Effect.Methods={morph:function(A,B){A=$(A);
new Effect.Morph(A,Object.extend({style:B},arguments[2]||{}));
return A
},visualEffect:function(C,E,B){C=$(C);
var D=E.dasherize().camelize(),A=D.charAt(0).toUpperCase()+D.substring(1);
new Effect[A](C,B);
return C
},highlight:function(B,A){B=$(B);
new Effect.Highlight(B,A);
return B
}};
$w("fade appear grow shrink fold blindUp blindDown slideUp slideDown pulsate shake puff squish switchOff dropOut").each(function(A){Effect.Methods[A]=function(C,B){C=$(C);
Effect[A.charAt(0).toUpperCase()+A.substring(1)](C,B);
return C
}
});
$w("getInlineOpacity forceRerendering setContentZoom collectTextNodes collectTextNodesIgnoreClass getStyles").each(function(A){Effect.Methods[A]=Element[A]
});
Element.addMethods(Effect.Methods);
if(typeof Effect=="undefined"){throw ("controls.js requires including script.aculo.us' effects.js library")
}var Autocompleter={};
Autocompleter.Base=Class.create({baseInitialize:function(B,C,A){B=$(B);
this.element=B;
this.update=$(C);
this.hasFocus=false;
this.changed=false;
this.active=false;
this.index=0;
this.entryCount=0;
this.oldElementValue=this.element.value;
if(this.setOptions){this.setOptions(A)
}else{this.options=A||{}
}this.options.paramName=this.options.paramName||this.element.name;
this.options.tokens=this.options.tokens||[];
this.options.frequency=this.options.frequency||0.4;
this.options.minChars=this.options.minChars||1;
this.options.onShow=this.options.onShow||function(D,E){if(!E.style.position||E.style.position=="absolute"){E.style.position="absolute";
Position.clone(D,E,{setHeight:false,offsetTop:D.offsetHeight})
}Effect.Appear(E,{duration:0.15})
};
this.options.onHide=this.options.onHide||function(D,E){new Effect.Fade(E,{duration:0.15})
};
if(typeof (this.options.tokens)=="string"){this.options.tokens=new Array(this.options.tokens)
}if(!this.options.tokens.include("\n")){this.options.tokens.push("\n")
}this.observer=null;
this.element.setAttribute("autocomplete","off");
Element.hide(this.update);
Event.observe(this.element,"blur",this.onBlur.bindAsEventListener(this));
Event.observe(this.element,"keydown",this.onKeyPress.bindAsEventListener(this))
},show:function(){if(Element.getStyle(this.update,"display")=="none"){this.options.onShow(this.element,this.update)
}if(!this.iefix&&(Prototype.Browser.IE)&&(Element.getStyle(this.update,"position")=="absolute")){new Insertion.After(this.update,'<iframe id="'+this.update.id+'_iefix" style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" src="javascript:false;" frameborder="0" scrolling="no"></iframe>');
this.iefix=$(this.update.id+"_iefix")
}if(this.iefix){setTimeout(this.fixIEOverlapping.bind(this),50)
}},fixIEOverlapping:function(){Position.clone(this.update,this.iefix,{setTop:(!this.update.style.height)});
this.iefix.style.zIndex=1;
this.update.style.zIndex=2;
Element.show(this.iefix)
},hide:function(){this.stopIndicator();
if(Element.getStyle(this.update,"display")!="none"){this.options.onHide(this.element,this.update)
}if(this.iefix){Element.hide(this.iefix)
}},startIndicator:function(){if(this.options.indicator){Element.show(this.options.indicator)
}},stopIndicator:function(){if(this.options.indicator){Element.hide(this.options.indicator)
}},onKeyPress:function(A){if(this.active){switch(A.keyCode){case Event.KEY_TAB:case Event.KEY_RETURN:this.selectEntry();
Event.stop(A);
case Event.KEY_ESC:this.hide();
this.active=false;
Event.stop(A);
return ;
case Event.KEY_LEFT:case Event.KEY_RIGHT:return ;
case Event.KEY_UP:this.markPrevious();
this.render();
Event.stop(A);
return ;
case Event.KEY_DOWN:this.markNext();
this.render();
Event.stop(A);
return 
}}else{if(A.keyCode==Event.KEY_TAB||A.keyCode==Event.KEY_RETURN||(Prototype.Browser.WebKit>0&&A.keyCode==0)){return 
}}this.changed=true;
this.hasFocus=true;
if(this.observer){clearTimeout(this.observer)
}this.observer=setTimeout(this.onObserverEvent.bind(this),this.options.frequency*1000)
},activate:function(){this.changed=false;
this.hasFocus=true;
this.getUpdatedChoices()
},onHover:function(B){var A=Event.findElement(B,"LI");
if(this.index!=A.autocompleteIndex){this.index=A.autocompleteIndex;
this.render()
}Event.stop(B)
},onClick:function(B){var A=Event.findElement(B,"LI");
this.index=A.autocompleteIndex;
this.selectEntry();
this.hide()
},onBlur:function(A){setTimeout(this.hide.bind(this),250);
this.hasFocus=false;
this.active=false
},render:function(){if(this.entryCount>0){for(var A=0;
A<this.entryCount;
A++){this.index==A?Element.addClassName(this.getEntry(A),"selected"):Element.removeClassName(this.getEntry(A),"selected")
}if(this.hasFocus){this.show();
this.active=true
}}else{this.active=false;
this.hide()
}},markPrevious:function(){if(this.index>0){this.index--
}else{this.index=this.entryCount-1;
this.update.scrollTop=this.update.scrollHeight
}selection=this.getEntry(this.index);
selection_top=selection.offsetTop;
if(selection_top<this.update.scrollTop){this.update.scrollTop=this.update.scrollTop-selection.offsetHeight
}},markNext:function(){if(this.index<this.entryCount-1){this.index++
}else{this.index=0;
this.update.scrollTop=0
}selection=this.getEntry(this.index);
selection_bottom=selection.offsetTop+selection.offsetHeight;
if(selection_bottom>this.update.scrollTop+this.update.offsetHeight){this.update.scrollTop=this.update.scrollTop+selection.offsetHeight
}},getEntry:function(A){return this.update.firstChild.childNodes[A]
},getCurrentEntry:function(){return this.getEntry(this.index)
},selectEntry:function(){this.active=false;
this.updateElement(this.getCurrentEntry())
},updateElement:function(F){if(this.options.updateElement){this.options.updateElement(F);
return 
}var D="";
if(this.options.select){var A=$(F).select("."+this.options.select)||[];
if(A.length>0){D=Element.collectTextNodes(A[0],this.options.select)
}}else{D=Element.collectTextNodesIgnoreClass(F,"informal")
}var C=this.getTokenBounds();
if(C[0]!=-1){var E=this.element.value.substr(0,C[0]);
var B=this.element.value.substr(C[0]).match(/^\s+/);
if(B){E+=B[0]
}this.element.value=E+D+this.element.value.substr(C[1])
}else{this.element.value=D
}this.oldElementValue=this.element.value;
if(this.options.afterUpdateElement){this.options.afterUpdateElement(this.element,F)
}},updateChoices:function(C){if(!this.changed&&this.hasFocus){this.update.innerHTML=C;
Element.cleanWhitespace(this.update);
Element.cleanWhitespace(this.update.down());
if(this.update.firstChild&&this.update.down().childNodes){this.entryCount=this.update.down().childNodes.length;
for(var A=0;
A<this.entryCount;
A++){var B=this.getEntry(A);
B.autocompleteIndex=A;
this.addObservers(B)
}}else{this.entryCount=0
}this.stopIndicator();
this.update.scrollTop=0;
this.index=0;
if(this.entryCount==1&&this.options.autoSelect){this.selectEntry();
this.hide()
}else{this.render()
}}},addObservers:function(A){Event.observe(A,"mouseover",this.onHover.bindAsEventListener(this));
Event.observe(A,"click",this.onClick.bindAsEventListener(this))
},onObserverEvent:function(){this.changed=false;
this.tokenBounds=null;
if(this.getToken().length>=this.options.minChars){this.getUpdatedChoices()
}else{this.active=false;
this.hide()
}this.oldElementValue=this.element.value
},getToken:function(){var A=this.getTokenBounds();
return this.element.value.substring(A[0],A[1]).strip()
},getTokenBounds:function(){if(null!=this.tokenBounds){return this.tokenBounds
}var E=this.element.value;
if(E.strip().empty()){return[-1,0]
}var F=arguments.callee.getFirstDifferencePos(E,this.oldElementValue);
var H=(F==this.oldElementValue.length?1:0);
var D=-1,C=E.length;
var G;
for(var B=0,A=this.options.tokens.length;
B<A;
++B){G=E.lastIndexOf(this.options.tokens[B],F+H-1);
if(G>D){D=G
}G=E.indexOf(this.options.tokens[B],F+H);
if(-1!=G&&G<C){C=G
}}return(this.tokenBounds=[D+1,C])
}});
Autocompleter.Base.prototype.getTokenBounds.getFirstDifferencePos=function(C,A){var D=Math.min(C.length,A.length);
for(var B=0;
B<D;
++B){if(C[B]!=A[B]){return B
}}return D
};
Ajax.Autocompleter=Class.create(Autocompleter.Base,{initialize:function(C,D,B,A){this.baseInitialize(C,D,A);
this.options.asynchronous=true;
this.options.onComplete=this.onComplete.bind(this);
this.options.defaultParams=this.options.parameters||null;
this.url=B
},getUpdatedChoices:function(){this.startIndicator();
var A=encodeURIComponent(this.options.paramName)+"="+encodeURIComponent(this.getToken());
this.options.parameters=this.options.callback?this.options.callback(this.element,A):A;
if(this.options.defaultParams){this.options.parameters+="&"+this.options.defaultParams
}new Ajax.Request(this.url,this.options)
},onComplete:function(A){this.updateChoices(A.responseText)
}});
Autocompleter.Local=Class.create(Autocompleter.Base,{initialize:function(B,D,C,A){this.baseInitialize(B,D,A);
this.options.array=C
},getUpdatedChoices:function(){this.updateChoices(this.options.selector(this))
},setOptions:function(A){this.options=Object.extend({choices:10,partialSearch:true,partialChars:2,ignoreCase:true,fullSearch:false,selector:function(B){var D=[];
var C=[];
var H=B.getToken();
var G=0;
for(var E=0;
E<B.options.array.length&&D.length<B.options.choices;
E++){var F=B.options.array[E];
var I=B.options.ignoreCase?F.toLowerCase().indexOf(H.toLowerCase()):F.indexOf(H);
while(I!=-1){if(I==0&&F.length!=H.length){D.push("<li><strong>"+F.substr(0,H.length)+"</strong>"+F.substr(H.length)+"</li>");
break
}else{if(H.length>=B.options.partialChars&&B.options.partialSearch&&I!=-1){if(B.options.fullSearch||/\s/.test(F.substr(I-1,1))){C.push("<li>"+F.substr(0,I)+"<strong>"+F.substr(I,H.length)+"</strong>"+F.substr(I+H.length)+"</li>");
break
}}}I=B.options.ignoreCase?F.toLowerCase().indexOf(H.toLowerCase(),I+1):F.indexOf(H,I+1)
}}if(C.length){D=D.concat(C.slice(0,B.options.choices-D.length))
}return"<ul>"+D.join("")+"</ul>"
}},A||{})
}});
Field.scrollFreeActivate=function(A){setTimeout(function(){Field.activate(A)
},1)
};
Ajax.InPlaceEditor=Class.create({initialize:function(C,B,A){this.url=B;
this.element=C=$(C);
this.prepareOptions();
this._controls={};
arguments.callee.dealWithDeprecatedOptions(A);
Object.extend(this.options,A||{});
if(!this.options.formId&&this.element.id){this.options.formId=this.element.id+"-inplaceeditor";
if($(this.options.formId)){this.options.formId=""
}}if(this.options.externalControl){this.options.externalControl=$(this.options.externalControl)
}if(!this.options.externalControl){this.options.externalControlOnly=false
}this._originalBackground=this.element.getStyle("background-color")||"transparent";
this.element.title=this.options.clickToEditText;
this._boundCancelHandler=this.handleFormCancellation.bind(this);
this._boundComplete=(this.options.onComplete||Prototype.emptyFunction).bind(this);
this._boundFailureHandler=this.handleAJAXFailure.bind(this);
this._boundSubmitHandler=this.handleFormSubmission.bind(this);
this._boundWrapperHandler=this.wrapUp.bind(this);
this.registerListeners()
},checkForEscapeOrReturn:function(A){if(!this._editing||A.ctrlKey||A.altKey||A.shiftKey){return 
}if(Event.KEY_ESC==A.keyCode){this.handleFormCancellation(A)
}else{if(Event.KEY_RETURN==A.keyCode){this.handleFormSubmission(A)
}}},createControl:function(G,C,B){var E=this.options[G+"Control"];
var F=this.options[G+"Text"];
if("button"==E){var A=document.createElement("input");
A.type="submit";
A.value=F;
A.className="editor_"+G+"_button";
if("cancel"==G){A.onclick=this._boundCancelHandler
}this._form.appendChild(A);
this._controls[G]=A
}else{if("link"==E){var D=document.createElement("a");
D.href="#";
D.appendChild(document.createTextNode(F));
D.onclick="cancel"==G?this._boundCancelHandler:this._boundSubmitHandler;
D.className="editor_"+G+"_link";
if(B){D.className+=" "+B
}this._form.appendChild(D);
this._controls[G]=D
}}},createEditField:function(){var C=(this.options.loadTextURL?this.options.loadingText:this.getText());
var B;
if(1>=this.options.rows&&!/\r|\n/.test(this.getText())){B=document.createElement("input");
B.type="text";
var A=this.options.size||this.options.cols||0;
if(0<A){B.size=A
}}else{B=document.createElement("textarea");
B.rows=(1>=this.options.rows?this.options.autoRows:this.options.rows);
B.cols=this.options.cols||40
}B.name=this.options.paramName;
B.value=C;
B.className="editor_field";
if(this.options.submitOnBlur){B.onblur=this._boundSubmitHandler
}this._controls.editor=B;
if(this.options.loadTextURL){this.loadExternalText()
}this._form.appendChild(this._controls.editor)
},createForm:function(){var B=this;
function A(D,E){var C=B.options["text"+D+"Controls"];
if(!C||E===false){return 
}B._form.appendChild(document.createTextNode(C))
}this._form=$(document.createElement("form"));
this._form.id=this.options.formId;
this._form.addClassName(this.options.formClassName);
this._form.onsubmit=this._boundSubmitHandler;
this.createEditField();
if("textarea"==this._controls.editor.tagName.toLowerCase()){this._form.appendChild(document.createElement("br"))
}if(this.options.onFormCustomization){this.options.onFormCustomization(this,this._form)
}A("Before",this.options.okControl||this.options.cancelControl);
this.createControl("ok",this._boundSubmitHandler);
A("Between",this.options.okControl&&this.options.cancelControl);
this.createControl("cancel",this._boundCancelHandler,"editor_cancel");
A("After",this.options.okControl||this.options.cancelControl)
},destroy:function(){if(this._oldInnerHTML){this.element.innerHTML=this._oldInnerHTML
}this.leaveEditMode();
this.unregisterListeners()
},enterEditMode:function(A){if(this._saving||this._editing){return 
}this._editing=true;
this.triggerCallback("onEnterEditMode");
if(this.options.externalControl){this.options.externalControl.hide()
}this.element.hide();
this.createForm();
this.element.parentNode.insertBefore(this._form,this.element);
if(!this.options.loadTextURL){this.postProcessEditField()
}if(A){Event.stop(A)
}},enterHover:function(A){if(this.options.hoverClassName){this.element.addClassName(this.options.hoverClassName)
}if(this._saving){return 
}this.triggerCallback("onEnterHover")
},getText:function(){return this.element.innerHTML.unescapeHTML()
},handleAJAXFailure:function(A){this.triggerCallback("onFailure",A);
if(this._oldInnerHTML){this.element.innerHTML=this._oldInnerHTML;
this._oldInnerHTML=null
}},handleFormCancellation:function(A){this.wrapUp();
if(A){Event.stop(A)
}},handleFormSubmission:function(D){var B=this._form;
var C=$F(this._controls.editor);
this.prepareSubmission();
var E=this.options.callback(B,C)||"";
if(Object.isString(E)){E=E.toQueryParams()
}E.editorId=this.element.id;
if(this.options.htmlResponse){var A=Object.extend({evalScripts:true},this.options.ajaxOptions);
Object.extend(A,{parameters:E,onComplete:this._boundWrapperHandler,onFailure:this._boundFailureHandler});
new Ajax.Updater({success:this.element},this.url,A)
}else{var A=Object.extend({method:"get"},this.options.ajaxOptions);
Object.extend(A,{parameters:E,onComplete:this._boundWrapperHandler,onFailure:this._boundFailureHandler});
new Ajax.Request(this.url,A)
}if(D){Event.stop(D)
}},leaveEditMode:function(){this.element.removeClassName(this.options.savingClassName);
this.removeForm();
this.leaveHover();
this.element.style.backgroundColor=this._originalBackground;
this.element.show();
if(this.options.externalControl){this.options.externalControl.show()
}this._saving=false;
this._editing=false;
this._oldInnerHTML=null;
this.triggerCallback("onLeaveEditMode")
},leaveHover:function(A){if(this.options.hoverClassName){this.element.removeClassName(this.options.hoverClassName)
}if(this._saving){return 
}this.triggerCallback("onLeaveHover")
},loadExternalText:function(){this._form.addClassName(this.options.loadingClassName);
this._controls.editor.disabled=true;
var A=Object.extend({method:"get"},this.options.ajaxOptions);
Object.extend(A,{parameters:"editorId="+encodeURIComponent(this.element.id),onComplete:Prototype.emptyFunction,onSuccess:function(C){this._form.removeClassName(this.options.loadingClassName);
var B=C.responseText;
if(this.options.stripLoadedTextTags){B=B.stripTags()
}this._controls.editor.value=B;
this._controls.editor.disabled=false;
this.postProcessEditField()
}.bind(this),onFailure:this._boundFailureHandler});
new Ajax.Request(this.options.loadTextURL,A)
},postProcessEditField:function(){var A=this.options.fieldPostCreation;
if(A){$(this._controls.editor)["focus"==A?"focus":"activate"]()
}},prepareOptions:function(){this.options=Object.clone(Ajax.InPlaceEditor.DefaultOptions);
Object.extend(this.options,Ajax.InPlaceEditor.DefaultCallbacks);
[this._extraDefaultOptions].flatten().compact().each(function(A){Object.extend(this.options,A)
}.bind(this))
},prepareSubmission:function(){this._saving=true;
this.removeForm();
this.leaveHover();
this.showSaving()
},registerListeners:function(){this._listeners={};
var A;
$H(Ajax.InPlaceEditor.Listeners).each(function(B){A=this[B.value].bind(this);
this._listeners[B.key]=A;
if(!this.options.externalControlOnly){this.element.observe(B.key,A)
}if(this.options.externalControl){this.options.externalControl.observe(B.key,A)
}}.bind(this))
},removeForm:function(){if(!this._form){return 
}this._form.remove();
this._form=null;
this._controls={}
},showSaving:function(){this._oldInnerHTML=this.element.innerHTML;
this.element.innerHTML=this.options.savingText;
this.element.addClassName(this.options.savingClassName);
this.element.style.backgroundColor=this._originalBackground;
this.element.show()
},triggerCallback:function(B,A){if("function"==typeof this.options[B]){this.options[B](this,A)
}},unregisterListeners:function(){$H(this._listeners).each(function(A){if(!this.options.externalControlOnly){this.element.stopObserving(A.key,A.value)
}if(this.options.externalControl){this.options.externalControl.stopObserving(A.key,A.value)
}}.bind(this))
},wrapUp:function(A){this.leaveEditMode();
this._boundComplete(A,this.element)
}});
Object.extend(Ajax.InPlaceEditor.prototype,{dispose:Ajax.InPlaceEditor.prototype.destroy});
Ajax.InPlaceCollectionEditor=Class.create(Ajax.InPlaceEditor,{initialize:function($super,C,B,A){this._extraDefaultOptions=Ajax.InPlaceCollectionEditor.DefaultOptions;
$super(C,B,A)
},createEditField:function(){var A=document.createElement("select");
A.name=this.options.paramName;
A.size=1;
this._controls.editor=A;
this._collection=this.options.collection||[];
if(this.options.loadCollectionURL){this.loadCollection()
}else{this.checkForExternalText()
}this._form.appendChild(this._controls.editor)
},loadCollection:function(){this._form.addClassName(this.options.loadingClassName);
this.showLoadingText(this.options.loadingCollectionText);
var options=Object.extend({method:"get"},this.options.ajaxOptions);
Object.extend(options,{parameters:"editorId="+encodeURIComponent(this.element.id),onComplete:Prototype.emptyFunction,onSuccess:function(transport){var js=transport.responseText.strip();
if(!/^\[.*\]$/.test(js)){throw ("Server returned an invalid collection representation.")
}this._collection=eval(js);
this.checkForExternalText()
}.bind(this),onFailure:this.onFailure});
new Ajax.Request(this.options.loadCollectionURL,options)
},showLoadingText:function(B){this._controls.editor.disabled=true;
var A=this._controls.editor.firstChild;
if(!A){A=document.createElement("option");
A.value="";
this._controls.editor.appendChild(A);
A.selected=true
}A.update((B||"").stripScripts().stripTags())
},checkForExternalText:function(){this._text=this.getText();
if(this.options.loadTextURL){this.loadExternalText()
}else{this.buildOptionList()
}},loadExternalText:function(){this.showLoadingText(this.options.loadingText);
var A=Object.extend({method:"get"},this.options.ajaxOptions);
Object.extend(A,{parameters:"editorId="+encodeURIComponent(this.element.id),onComplete:Prototype.emptyFunction,onSuccess:function(B){this._text=B.responseText.strip();
this.buildOptionList()
}.bind(this),onFailure:this.onFailure});
new Ajax.Request(this.options.loadTextURL,A)
},buildOptionList:function(){this._form.removeClassName(this.options.loadingClassName);
this._collection=this._collection.map(function(D){return 2===D.length?D:[D,D].flatten()
});
var B=("value" in this.options)?this.options.value:this._text;
var A=this._collection.any(function(D){return D[0]==B
}.bind(this));
this._controls.editor.update("");
var C;
this._collection.each(function(E,D){C=document.createElement("option");
C.value=E[0];
C.selected=A?E[0]==B:0==D;
C.appendChild(document.createTextNode(E[1]));
this._controls.editor.appendChild(C)
}.bind(this));
this._controls.editor.disabled=false;
Field.scrollFreeActivate(this._controls.editor)
}});
Ajax.InPlaceEditor.prototype.initialize.dealWithDeprecatedOptions=function(A){if(!A){return 
}function B(C,D){if(C in A||D===undefined){return 
}A[C]=D
}B("cancelControl",(A.cancelLink?"link":(A.cancelButton?"button":A.cancelLink==A.cancelButton==false?false:undefined)));
B("okControl",(A.okLink?"link":(A.okButton?"button":A.okLink==A.okButton==false?false:undefined)));
B("highlightColor",A.highlightcolor);
B("highlightEndColor",A.highlightendcolor)
};
Object.extend(Ajax.InPlaceEditor,{DefaultOptions:{ajaxOptions:{},autoRows:3,cancelControl:"link",cancelText:"cancel",clickToEditText:"Click to edit",externalControl:null,externalControlOnly:false,fieldPostCreation:"activate",formClassName:"inplaceeditor-form",formId:null,highlightColor:"#ffff99",highlightEndColor:"#ffffff",hoverClassName:"",htmlResponse:true,loadingClassName:"inplaceeditor-loading",loadingText:"Loading...",okControl:"button",okText:"ok",paramName:"value",rows:1,savingClassName:"inplaceeditor-saving",savingText:"Saving...",size:0,stripLoadedTextTags:false,submitOnBlur:false,textAfterControls:"",textBeforeControls:"",textBetweenControls:""},DefaultCallbacks:{callback:function(A){return Form.serialize(A)
},onComplete:function(B,A){new Effect.Highlight(A,{startcolor:this.options.highlightColor,keepBackgroundImage:true})
},onEnterEditMode:null,onEnterHover:function(A){A.element.style.backgroundColor=A.options.highlightColor;
if(A._effect){A._effect.cancel()
}},onFailure:function(B,A){alert("Error communication with the server: "+B.responseText.stripTags())
},onFormCustomization:null,onLeaveEditMode:null,onLeaveHover:function(A){A._effect=new Effect.Highlight(A.element,{startcolor:A.options.highlightColor,endcolor:A.options.highlightEndColor,restorecolor:A._originalBackground,keepBackgroundImage:true})
}},Listeners:{click:"enterEditMode",keydown:"checkForEscapeOrReturn",mouseover:"enterHover",mouseout:"leaveHover"}});
Ajax.InPlaceCollectionEditor.DefaultOptions={loadingCollectionText:"Loading options..."};
Form.Element.DelayedObserver=Class.create({initialize:function(B,A,C){this.delay=A||0.5;
this.element=$(B);
this.callback=C;
this.timer=null;
this.lastValue=$F(this.element);
Event.observe(this.element,"keyup",this.delayedListener.bindAsEventListener(this))
},delayedListener:function(A){if(this.lastValue==$F(this.element)){return 
}if(this.timer){clearTimeout(this.timer)
}this.timer=setTimeout(this.onTimerEvent.bind(this),this.delay*1000);
this.lastValue=$F(this.element)
},onTimerEvent:function(){this.timer=null;
this.callback(this.element,$F(this.element))
}});
if(Object.isUndefined(Effect)){throw ("dragdrop.js requires including script.aculo.us' effects.js library")
}var Droppables={drops:[],remove:function(A){this.drops=this.drops.reject(function(B){return B.element==$(A)
})
},add:function(B){B=$(B);
var A=Object.extend({greedy:true,hoverclass:null,tree:false},arguments[1]||{});
if(A.containment){A._containers=[];
var C=A.containment;
if(Object.isArray(C)){C.each(function(D){A._containers.push($(D))
})
}else{A._containers.push($(C))
}}if(A.accept){A.accept=[A.accept].flatten()
}Element.makePositioned(B);
A.element=B;
this.drops.push(A)
},findDeepestChild:function(A){deepest=A[0];
for(i=1;
i<A.length;
++i){if(Element.isParent(A[i].element,deepest.element)){deepest=A[i]
}}return deepest
},isContained:function(B,A){var C;
if(A.tree){C=B.treeNode
}else{C=B.parentNode
}return A._containers.detect(function(D){return C==D
})
},isAffected:function(A,C,B){return((B.element!=C)&&((!B._containers)||this.isContained(C,B))&&((!B.accept)||(Element.classNames(C).detect(function(D){return B.accept.include(D)
})))&&Position.within(B.element,A[0],A[1]))
},deactivate:function(A){if(A.hoverclass){Element.removeClassName(A.element,A.hoverclass)
}this.last_active=null
},activate:function(A){if(A.hoverclass){Element.addClassName(A.element,A.hoverclass)
}this.last_active=A
},show:function(A,C){if(!this.drops.length){return 
}var B,D=[];
this.drops.each(function(E){if(Droppables.isAffected(A,C,E)){D.push(E)
}});
if(D.length>0){B=Droppables.findDeepestChild(D)
}if(this.last_active&&this.last_active!=B){this.deactivate(this.last_active)
}if(B){Position.within(B.element,A[0],A[1]);
if(B.onHover){B.onHover(C,B.element,Position.overlap(B.overlap,B.element))
}if(B!=this.last_active){Droppables.activate(B)
}}},fire:function(B,A){if(!this.last_active){return 
}Position.prepare();
if(this.isAffected([Event.pointerX(B),Event.pointerY(B)],A,this.last_active)){if(this.last_active.onDrop){this.last_active.onDrop(A,this.last_active.element,B);
return true
}}},reset:function(){if(this.last_active){this.deactivate(this.last_active)
}}};
var Draggables={drags:[],observers:[],register:function(A){if(this.drags.length==0){this.eventMouseUp=this.endDrag.bindAsEventListener(this);
this.eventMouseMove=this.updateDrag.bindAsEventListener(this);
this.eventKeypress=this.keyPress.bindAsEventListener(this);
Event.observe(document,"mouseup",this.eventMouseUp);
Event.observe(document,"mousemove",this.eventMouseMove);
Event.observe(document,"keypress",this.eventKeypress)
}this.drags.push(A)
},unregister:function(A){this.drags=this.drags.reject(function(B){return B==A
});
if(this.drags.length==0){Event.stopObserving(document,"mouseup",this.eventMouseUp);
Event.stopObserving(document,"mousemove",this.eventMouseMove);
Event.stopObserving(document,"keypress",this.eventKeypress)
}},activate:function(A){if(A.options.delay){this._timeout=setTimeout(function(){Draggables._timeout=null;
window.focus();
Draggables.activeDraggable=A
}.bind(this),A.options.delay)
}else{window.focus();
this.activeDraggable=A
}},deactivate:function(){this.activeDraggable=null
},updateDrag:function(A){if(!this.activeDraggable){return 
}var B=[Event.pointerX(A),Event.pointerY(A)];
if(this._lastPointer&&(this._lastPointer.inspect()==B.inspect())){return 
}this._lastPointer=B;
this.activeDraggable.updateDrag(A,B)
},endDrag:function(A){if(this._timeout){clearTimeout(this._timeout);
this._timeout=null
}if(!this.activeDraggable){return 
}this._lastPointer=null;
this.activeDraggable.endDrag(A);
this.activeDraggable=null
},keyPress:function(A){if(this.activeDraggable){this.activeDraggable.keyPress(A)
}},addObserver:function(A){this.observers.push(A);
this._cacheObserverCallbacks()
},removeObserver:function(A){this.observers=this.observers.reject(function(B){return B.element==A
});
this._cacheObserverCallbacks()
},notify:function(B,A,C){if(this[B+"Count"]>0){this.observers.each(function(D){if(D[B]){D[B](B,A,C)
}})
}if(A.options[B]){A.options[B](A,C)
}},_cacheObserverCallbacks:function(){["onStart","onEnd","onDrag"].each(function(A){Draggables[A+"Count"]=Draggables.observers.select(function(B){return B[A]
}).length
})
}};
var Draggable=Class.create({initialize:function(B){var C={handle:false,reverteffect:function(F,E,D){var G=Math.sqrt(Math.abs(E^2)+Math.abs(D^2))*0.02;
new Effect.Move(F,{x:-D,y:-E,duration:G,queue:{scope:"_draggable",position:"end"}})
},endeffect:function(E){var D=Object.isNumber(E._opacity)?E._opacity:1;
new Effect.Opacity(E,{duration:0.2,from:0.7,to:D,queue:{scope:"_draggable",position:"end"},afterFinish:function(){Draggable._dragging[E]=false
}})
},zindex:1000,revert:false,quiet:false,scroll:false,scrollSensitivity:20,scrollSpeed:15,snap:false,delay:0};
if(!arguments[1]||Object.isUndefined(arguments[1].endeffect)){Object.extend(C,{starteffect:function(D){D._opacity=Element.getOpacity(D);
Draggable._dragging[D]=true;
new Effect.Opacity(D,{duration:0.2,from:D._opacity,to:0.7})
}})
}var A=Object.extend(C,arguments[1]||{});
this.element=$(B);
if(A.handle&&Object.isString(A.handle)){this.handle=this.element.down("."+A.handle,0)
}if(!this.handle){this.handle=$(A.handle)
}if(!this.handle){this.handle=this.element
}if(A.scroll&&!A.scroll.scrollTo&&!A.scroll.outerHTML){A.scroll=$(A.scroll);
this._isScrollChild=Element.childOf(this.element,A.scroll)
}Element.makePositioned(this.element);
this.options=A;
this.dragging=false;
this.eventMouseDown=this.initDrag.bindAsEventListener(this);
Event.observe(this.handle,"mousedown",this.eventMouseDown);
Draggables.register(this)
},destroy:function(){Event.stopObserving(this.handle,"mousedown",this.eventMouseDown);
Draggables.unregister(this)
},currentDelta:function(){return([parseInt(Element.getStyle(this.element,"left")||"0"),parseInt(Element.getStyle(this.element,"top")||"0")])
},initDrag:function(A){if(!Object.isUndefined(Draggable._dragging[this.element])&&Draggable._dragging[this.element]){return 
}if(Event.isLeftClick(A)){var C=Event.element(A);
if((tag_name=C.tagName.toUpperCase())&&(tag_name=="INPUT"||tag_name=="SELECT"||tag_name=="OPTION"||tag_name=="BUTTON"||tag_name=="TEXTAREA")){return 
}var B=[Event.pointerX(A),Event.pointerY(A)];
var D=this.element.cumulativeOffset();
this.offset=[0,1].map(function(E){return(B[E]-D[E])
});
Draggables.activate(this);
Event.stop(A)
}},startDrag:function(B){this.dragging=true;
if(!this.delta){this.delta=this.currentDelta()
}if(this.options.zindex){this.originalZ=parseInt(Element.getStyle(this.element,"z-index")||0);
this.element.style.zIndex=this.options.zindex
}if(this.options.ghosting){this._clone=this.element.cloneNode(true);
this._originallyAbsolute=(this.element.getStyle("position")=="absolute");
if(!this._originallyAbsolute){Position.absolutize(this.element)
}this.element.parentNode.insertBefore(this._clone,this.element)
}if(this.options.scroll){if(this.options.scroll==window){var A=this._getWindowScroll(this.options.scroll);
this.originalScrollLeft=A.left;
this.originalScrollTop=A.top
}else{this.originalScrollLeft=this.options.scroll.scrollLeft;
this.originalScrollTop=this.options.scroll.scrollTop
}}Draggables.notify("onStart",this,B);
if(this.options.starteffect){this.options.starteffect(this.element)
}},updateDrag:function(event,pointer){if(!this.dragging){this.startDrag(event)
}if(!this.options.quiet){Position.prepare();
Droppables.show(pointer,this.element)
}Draggables.notify("onDrag",this,event);
this.draw(pointer);
if(this.options.change){this.options.change(this)
}if(this.options.scroll){this.stopScrolling();
var p;
if(this.options.scroll==window){with(this._getWindowScroll(this.options.scroll)){p=[left,top,left+width,top+height]
}}else{p=Position.page(this.options.scroll).toArray();
p[0]+=this.options.scroll.scrollLeft+Position.deltaX;
p[1]+=this.options.scroll.scrollTop+Position.deltaY;
p.push(p[0]+this.options.scroll.offsetWidth);
p.push(p[1]+this.options.scroll.offsetHeight)
}var speed=[0,0];
if(pointer[0]<(p[0]+this.options.scrollSensitivity)){speed[0]=pointer[0]-(p[0]+this.options.scrollSensitivity)
}if(pointer[1]<(p[1]+this.options.scrollSensitivity)){speed[1]=pointer[1]-(p[1]+this.options.scrollSensitivity)
}if(pointer[0]>(p[2]-this.options.scrollSensitivity)){speed[0]=pointer[0]-(p[2]-this.options.scrollSensitivity)
}if(pointer[1]>(p[3]-this.options.scrollSensitivity)){speed[1]=pointer[1]-(p[3]-this.options.scrollSensitivity)
}this.startScrolling(speed)
}if(Prototype.Browser.WebKit){window.scrollBy(0,0)
}Event.stop(event)
},finishDrag:function(B,E){this.dragging=false;
if(this.options.quiet){Position.prepare();
var D=[Event.pointerX(B),Event.pointerY(B)];
Droppables.show(D,this.element)
}if(this.options.ghosting){if(!this._originallyAbsolute){Position.relativize(this.element)
}delete this._originallyAbsolute;
Element.remove(this._clone);
this._clone=null
}var F=false;
if(E){F=Droppables.fire(B,this.element);
if(!F){F=false
}}if(F&&this.options.onDropped){this.options.onDropped(this.element)
}Draggables.notify("onEnd",this,B);
var A=this.options.revert;
if(A&&Object.isFunction(A)){A=A(this.element)
}var C=this.currentDelta();
if(A&&this.options.reverteffect){if(F==0||A!="failure"){this.options.reverteffect(this.element,C[1]-this.delta[1],C[0]-this.delta[0])
}}else{this.delta=C
}if(this.options.zindex){this.element.style.zIndex=this.originalZ
}if(this.options.endeffect){this.options.endeffect(this.element)
}Draggables.deactivate(this);
Droppables.reset()
},keyPress:function(A){if(A.keyCode!=Event.KEY_ESC){return 
}this.finishDrag(A,false);
Event.stop(A)
},endDrag:function(A){if(!this.dragging){return 
}this.stopScrolling();
this.finishDrag(A,true);
Event.stop(A)
},draw:function(A){var F=this.element.cumulativeOffset();
if(this.options.ghosting){var C=Position.realOffset(this.element);
F[0]+=C[0]-Position.deltaX;
F[1]+=C[1]-Position.deltaY
}var E=this.currentDelta();
F[0]-=E[0];
F[1]-=E[1];
if(this.options.scroll&&(this.options.scroll!=window&&this._isScrollChild)){F[0]-=this.options.scroll.scrollLeft-this.originalScrollLeft;
F[1]-=this.options.scroll.scrollTop-this.originalScrollTop
}var D=[0,1].map(function(G){return(A[G]-F[G]-this.offset[G])
}.bind(this));
if(this.options.snap){if(Object.isFunction(this.options.snap)){D=this.options.snap(D[0],D[1],this)
}else{if(Object.isArray(this.options.snap)){D=D.map(function(G,H){return(G/this.options.snap[H]).round()*this.options.snap[H]
}.bind(this))
}else{D=D.map(function(G){return(G/this.options.snap).round()*this.options.snap
}.bind(this))
}}}var B=this.element.style;
if((!this.options.constraint)||(this.options.constraint=="horizontal")){B.left=D[0]+"px"
}if((!this.options.constraint)||(this.options.constraint=="vertical")){B.top=D[1]+"px"
}if(B.visibility=="hidden"){B.visibility=""
}},stopScrolling:function(){if(this.scrollInterval){clearInterval(this.scrollInterval);
this.scrollInterval=null;
Draggables._lastScrollPointer=null
}},startScrolling:function(A){if(!(A[0]||A[1])){return 
}this.scrollSpeed=[A[0]*this.options.scrollSpeed,A[1]*this.options.scrollSpeed];
this.lastScrolled=new Date();
this.scrollInterval=setInterval(this.scroll.bind(this),10)
},scroll:function(){var current=new Date();
var delta=current-this.lastScrolled;
this.lastScrolled=current;
if(this.options.scroll==window){with(this._getWindowScroll(this.options.scroll)){if(this.scrollSpeed[0]||this.scrollSpeed[1]){var d=delta/1000;
this.options.scroll.scrollTo(left+d*this.scrollSpeed[0],top+d*this.scrollSpeed[1])
}}}else{this.options.scroll.scrollLeft+=this.scrollSpeed[0]*delta/1000;
this.options.scroll.scrollTop+=this.scrollSpeed[1]*delta/1000
}Position.prepare();
Droppables.show(Draggables._lastPointer,this.element);
Draggables.notify("onDrag",this);
if(this._isScrollChild){Draggables._lastScrollPointer=Draggables._lastScrollPointer||$A(Draggables._lastPointer);
Draggables._lastScrollPointer[0]+=this.scrollSpeed[0]*delta/1000;
Draggables._lastScrollPointer[1]+=this.scrollSpeed[1]*delta/1000;
if(Draggables._lastScrollPointer[0]<0){Draggables._lastScrollPointer[0]=0
}if(Draggables._lastScrollPointer[1]<0){Draggables._lastScrollPointer[1]=0
}this.draw(Draggables._lastScrollPointer)
}if(this.options.change){this.options.change(this)
}},_getWindowScroll:function(w){var T,L,W,H;
with(w.document){if(w.document.documentElement&&documentElement.scrollTop){T=documentElement.scrollTop;
L=documentElement.scrollLeft
}else{if(w.document.body){T=body.scrollTop;
L=body.scrollLeft
}}if(w.innerWidth){W=w.innerWidth;
H=w.innerHeight
}else{if(w.document.documentElement&&documentElement.clientWidth){W=documentElement.clientWidth;
H=documentElement.clientHeight
}else{W=body.offsetWidth;
H=body.offsetHeight
}}}return{top:T,left:L,width:W,height:H}
}});
Draggable._dragging={};
var SortableObserver=Class.create({initialize:function(B,A){this.element=$(B);
this.observer=A;
this.lastValue=Sortable.serialize(this.element)
},onStart:function(){this.lastValue=Sortable.serialize(this.element)
},onEnd:function(){Sortable.unmark();
if(this.lastValue!=Sortable.serialize(this.element)){this.observer(this.element)
}}});
var Sortable={SERIALIZE_RULE:/^[^_\-](?:[A-Za-z0-9\-\_]*)[_](.*)$/,sortables:{},_findRootElement:function(A){while(A.tagName.toUpperCase()!="BODY"){if(A.id&&Sortable.sortables[A.id]){return A
}A=A.parentNode
}},options:function(A){A=Sortable._findRootElement($(A));
if(!A){return 
}return Sortable.sortables[A.id]
},destroy:function(A){A=$(A);
var B=Sortable.sortables[A.id];
if(B){Draggables.removeObserver(B.element);
B.droppables.each(function(C){Droppables.remove(C)
});
B.draggables.invoke("destroy");
delete Sortable.sortables[B.element.id]
}},create:function(C){C=$(C);
var B=Object.extend({element:C,tag:"li",dropOnEmpty:false,tree:false,treeTag:"ul",overlap:"vertical",constraint:"vertical",containment:C,handle:false,only:false,delay:0,hoverclass:null,ghosting:false,quiet:false,scroll:false,scrollSensitivity:20,scrollSpeed:15,format:this.SERIALIZE_RULE,elements:false,handles:false,onChange:Prototype.emptyFunction,onUpdate:Prototype.emptyFunction},arguments[1]||{});
this.destroy(C);
var A={revert:true,quiet:B.quiet,scroll:B.scroll,scrollSpeed:B.scrollSpeed,scrollSensitivity:B.scrollSensitivity,delay:B.delay,ghosting:B.ghosting,constraint:B.constraint,handle:B.handle};
if(B.starteffect){A.starteffect=B.starteffect
}if(B.reverteffect){A.reverteffect=B.reverteffect
}else{if(B.ghosting){A.reverteffect=function(F){F.style.top=0;
F.style.left=0
}
}}if(B.endeffect){A.endeffect=B.endeffect
}if(B.zindex){A.zindex=B.zindex
}var D={overlap:B.overlap,containment:B.containment,tree:B.tree,hoverclass:B.hoverclass,onHover:Sortable.onHover};
var E={onHover:Sortable.onEmptyHover,overlap:B.overlap,containment:B.containment,hoverclass:B.hoverclass};
Element.cleanWhitespace(C);
B.draggables=[];
B.droppables=[];
if(B.dropOnEmpty||B.tree){Droppables.add(C,E);
B.droppables.push(C)
}(B.elements||this.findElements(C,B)||[]).each(function(H,F){var G=B.handles?$(B.handles[F]):(B.handle?$(H).select("."+B.handle)[0]:H);
B.draggables.push(new Draggable(H,Object.extend(A,{handle:G})));
Droppables.add(H,D);
if(B.tree){H.treeNode=C
}B.droppables.push(H)
});
if(B.tree){(Sortable.findTreeElements(C,B)||[]).each(function(F){Droppables.add(F,E);
F.treeNode=C;
B.droppables.push(F)
})
}this.sortables[C.identify()]=B;
Draggables.addObserver(new SortableObserver(C,B.onUpdate))
},findElements:function(B,A){return Element.findChildren(B,A.only,A.tree?true:false,A.tag)
},findTreeElements:function(B,A){return Element.findChildren(B,A.only,A.tree?true:false,A.treeTag)
},onHover:function(E,D,A){if(Element.isParent(D,E)){return 
}if(A>0.33&&A<0.66&&Sortable.options(D).tree){return 
}else{if(A>0.5){Sortable.mark(D,"before");
if(D.previousSibling!=E){var B=E.parentNode;
E.style.visibility="hidden";
D.parentNode.insertBefore(E,D);
if(D.parentNode!=B){Sortable.options(B).onChange(E)
}Sortable.options(D.parentNode).onChange(E)
}}else{Sortable.mark(D,"after");
var C=D.nextSibling||null;
if(C!=E){var B=E.parentNode;
E.style.visibility="hidden";
D.parentNode.insertBefore(E,C);
if(D.parentNode!=B){Sortable.options(B).onChange(E)
}Sortable.options(D.parentNode).onChange(E)
}}}},onEmptyHover:function(E,G,H){var I=E.parentNode;
var A=Sortable.options(G);
if(!Element.isParent(G,E)){var F;
var C=Sortable.findElements(G,{tag:A.tag,only:A.only});
var B=null;
if(C){var D=Element.offsetSize(G,A.overlap)*(1-H);
for(F=0;
F<C.length;
F+=1){if(D-Element.offsetSize(C[F],A.overlap)>=0){D-=Element.offsetSize(C[F],A.overlap)
}else{if(D-(Element.offsetSize(C[F],A.overlap)/2)>=0){B=F+1<C.length?C[F+1]:null;
break
}else{B=C[F];
break
}}}}G.insertBefore(E,B);
Sortable.options(I).onChange(E);
A.onChange(E)
}},unmark:function(){if(Sortable._marker){Sortable._marker.hide()
}},mark:function(B,A){var D=Sortable.options(B.parentNode);
if(D&&!D.ghosting){return 
}if(!Sortable._marker){Sortable._marker=($("dropmarker")||Element.extend(document.createElement("DIV"))).hide().addClassName("dropmarker").setStyle({position:"absolute"});
document.getElementsByTagName("body").item(0).appendChild(Sortable._marker)
}var C=B.cumulativeOffset();
Sortable._marker.setStyle({left:C[0]+"px",top:C[1]+"px"});
if(A=="after"){if(D.overlap=="horizontal"){Sortable._marker.setStyle({left:(C[0]+B.clientWidth)+"px"})
}else{Sortable._marker.setStyle({top:(C[1]+B.clientHeight)+"px"})
}}Sortable._marker.show()
},_tree:function(E,B,F){var D=Sortable.findElements(E,B)||[];
for(var C=0;
C<D.length;
++C){var A=D[C].id.match(B.format);
if(!A){continue
}var G={id:encodeURIComponent(A?A[1]:null),element:E,parent:F,children:[],position:F.children.length,container:$(D[C]).down(B.treeTag)};
if(G.container){this._tree(G.container,B,G)
}F.children.push(G)
}return F
},tree:function(D){D=$(D);
var C=this.options(D);
var B=Object.extend({tag:C.tag,treeTag:C.treeTag,only:C.only,name:D.id,format:C.format},arguments[1]||{});
var A={id:null,parent:null,children:[],container:D,position:0};
return Sortable._tree(D,B,A)
},_constructIndex:function(B){var A="";
do{if(B.id){A="["+B.position+"]"+A
}}while((B=B.parent)!=null);
return A
},sequence:function(B){B=$(B);
var A=Object.extend(this.options(B),arguments[1]||{});
return $(this.findElements(B,A)||[]).map(function(C){return C.id.match(A.format)?C.id.match(A.format)[1]:""
})
},setSequence:function(B,C){B=$(B);
var A=Object.extend(this.options(B),arguments[2]||{});
var D={};
this.findElements(B,A).each(function(E){if(E.id.match(A.format)){D[E.id.match(A.format)[1]]=[E,E.parentNode]
}E.parentNode.removeChild(E)
});
C.each(function(E){var F=D[E];
if(F){F[1].appendChild(F[0]);
delete D[E]
}})
},serialize:function(C){C=$(C);
var B=Object.extend(Sortable.options(C),arguments[1]||{});
var A=encodeURIComponent((arguments[1]&&arguments[1].name)?arguments[1].name:C.id);
if(B.tree){return Sortable.tree(C,arguments[1]).children.map(function(D){return[A+Sortable._constructIndex(D)+"[id]="+encodeURIComponent(D.id)].concat(D.children.map(arguments.callee))
}).flatten().join("&")
}else{return Sortable.sequence(C,arguments[1]).map(function(D){return A+"[]="+encodeURIComponent(D)
}).join("&")
}}};
Element.isParent=function(B,A){if(!B.parentNode||B==A){return false
}if(B.parentNode==A){return true
}return Element.isParent(B.parentNode,A)
};
Element.findChildren=function(D,B,A,C){if(!D.hasChildNodes()){return null
}C=C.toUpperCase();
if(B){B=[B].flatten()
}var E=[];
$A(D.childNodes).each(function(G){if(G.tagName&&G.tagName.toUpperCase()==C&&(!B||(Element.classNames(G).detect(function(H){return B.include(H)
})))){E.push(G)
}if(A){var F=Element.findChildren(G,B,A,C);
if(F){E.push(F)
}}});
return(E.length>0?E.flatten():[])
};
Element.offsetSize=function(A,B){return A["offset"+((B=="vertical"||B=="height")?"Height":"Width")]
};var TableKit=Class.create();
TableKit.prototype={initialize:function(D,A){var B=$(D);
if(B.tagName!=="TABLE"){return 
}TableKit.register(B,Object.extend(TableKit.options,A||{}));
this.id=B.id;
var C=TableKit.option("sortable resizable editable",this.id);
if(C.sortable){TableKit.Sortable.init(B)
}if(C.resizable){TableKit.Resizable.init(B)
}if(C.editable){TableKit.Editable.init(B)
}},sort:function(B,A){TableKit.Sortable.sort(this.id,B,A)
},resizeColumn:function(B,A){TableKit.Resizable.resize(this.id,B,A)
},editCell:function(B,A){TableKit.Editable.editCell(this.id,B,A)
}};
Object.extend(TableKit,{getBodyRows:function(A){A=$(A);
var B=A.id;
if(!TableKit.rows[B]){TableKit.rows[B]=(A.tHead&&A.tHead.rows.length>0)?$A(A.tBodies[0].rows):$A(A.rows).without(A.rows[0])
}return TableKit.rows[B]
},getHeaderCells:function(B,A){if(!B){B=$(A).up("table")
}var C=B.id;
if(!TableKit.heads[C]){TableKit.heads[C]=$A((B.tHead&&B.tHead.rows.length>0)?B.tHead.rows[B.tHead.rows.length-1].cells:B.rows[0].cells)
}return TableKit.heads[C]
},getCellIndex:function(A){return $A(A.parentNode.cells).indexOf(A)
},getRowIndex:function(A){return $A(A.parentNode.rows).indexOf(A)
},getCellText:function(A,B){if(!A){return""
}TableKit.registerCell(A);
var C=TableKit.cells[A.id];
if(B||C.refresh||!C.textContent){x=$(A).readAttribute("x");
if(x!=null&&x!=""){C.textContent=x
}else{C.textContent=A.textContent?A.textContent:A.innerText
}C.refresh=false
}return C.textContent
},register:function(B,A){if(!B.id){TableKit._tblcount+=1;
B.id="tablekit-table-"+TableKit._tblcount
}var C=B.id;
TableKit.tables[C]=TableKit.tables[C]?Object.extend(TableKit.tables[C],A||{}):Object.extend({sortable:false,resizable:false,editable:false},A||{})
},registerCell:function(A){if(!A.id){TableKit._cellcount+=1;
A.id="tablekit-cell-"+TableKit._cellcount
}if(!TableKit.cells[A.id]){TableKit.cells[A.id]={textContent:"",htmlContent:"",active:false}
}},isSortable:function(A){return TableKit.tables[A.id]?TableKit.tables[A.id].sortable:false
},isResizable:function(A){return TableKit.tables[A.id]?TableKit.tables[A.id].resizable:false
},isEditable:function(A){return TableKit.tables[A.id]?TableKit.tables[A.id].editable:false
},setup:function(A){Object.extend(TableKit.options,A||{})
},option:function(B,E,D,C){D=D||TableKit.options;
C=C||(E?(TableKit.tables[E]?TableKit.tables[E]:{}):{});
var A=E+B;
if(!TableKit._opcache[A]){TableKit._opcache[A]=$A($w(B)).inject([],function(F,G){F.push(F[G]=C[G]||D[G]);
return F
})
}return TableKit._opcache[A]
},e:function(A){return A||window.event
},tables:{},_opcache:{},cells:{},rows:{},heads:{},options:{autoLoad:false,stripe:true,sortable:true,resizable:true,editable:true,rowEvenClass:"roweven",rowOddClass:"rowodd",sortableSelector:["table.sortable"],columnClass:"sortcol",descendingClass:"sortdesc",ascendingClass:"sortasc",noSortClass:"nosort",sortFirstAscendingClass:"sortfirstasc",sortFirstDecendingClass:"sortfirstdesc",resizableSelector:["table.resizable"],minWidth:10,showHandle:true,resizeOnHandleClass:"resize-handle-active",editableSelector:["table.editable"],formClassName:"editable-cell-form",noEditClass:"noedit",editAjaxURI:"/",editAjaxOptions:{}},_tblcount:0,_cellcount:0,load:function(){if(TableKit.options.autoLoad){if(TableKit.options.sortable){$A(TableKit.options.sortableSelector).each(function(A){$$(A).each(function(B){TableKit.Sortable.init(B)
})
})
}if(TableKit.options.resizable){$A(TableKit.options.resizableSelector).each(function(A){$$(A).each(function(B){TableKit.Resizable.init(B)
})
})
}if(TableKit.options.editable){$A(TableKit.options.editableSelector).each(function(A){$$(A).each(function(B){TableKit.Editable.init(B)
})
})
}}}});
TableKit.Rows={stripe:function(A){var B=TableKit.getBodyRows(A);
B.each(function(D,C){TableKit.Rows.addStripeClass(A,D,C)
})
},addStripeClass:function(I,A,C){I=I||A.up("table");
var D=TableKit.option("rowEvenClass rowOddClass",I.id);
var E=((C+1)%2===0?D[0]:D[1]);
var F=A.className.split(/\s+/);
var H=[];
for(var G=0,B=F.length;
G<B;
G+=1){if(F[G]!==D[0]&&F[G]!==D[1]){H.push(F[G])
}}H.push(E);
A.className=H.join(" ")
}};
TableKit.Sortable={init:function(F,C){var D=$(F);
if(D.tagName!=="TABLE"){return 
}TableKit.register(D,Object.extend(C||{},{sortable:true}));
var B;
var A=TableKit.getHeaderCells(D);
var E=TableKit.option("noSortClass columnClass sortFirstAscendingClass sortFirstDecendingClass",D.id);
A.each(function(G){G=$(G);
if(!G.hasClassName(E.noSortClass)){Event.observe(G,"mousedown",TableKit.Sortable._sort);
G.addClassName(E.columnClass);
if(G.hasClassName(E.sortFirstAscendingClass)||G.hasClassName(E.sortFirstDecendingClass)){B=G
}}});
if(B){if(B.hasClassName(E.sortFirstAscendingClass)){TableKit.Sortable.sort(D,B,1)
}else{TableKit.Sortable.sort(D,B,-1)
}}else{TableKit.Rows.stripe(D)
}},reload:function(B){B=$(B);
var A=TableKit.getHeaderCells(B);
var C=TableKit.option("noSortClass columnClass",B.id);
A.each(function(D){D=$(D);
if(!D.hasClassName(C.noSortClass)){Event.stopObserving(D,"mousedown",TableKit.Sortable._sort);
D.removeClassName(C.columnClass)
}});
TableKit.Sortable.init(B)
},_sort:function(B){if(TableKit.Resizable._onHandle){return 
}B=TableKit.e(B);
Event.stop(B);
var A=Event.element(B);
while(!(A.tagName&&A.tagName.match(/td|th/gi))){A=A.parentNode
}TableKit.Sortable.sort(null,A)
},sort:function(J,F,B){var H;
if(typeof F==="number"){if(!J||(J.tagName&&J.tagName!=="TABLE")){return 
}J=$(J);
F=Math.min(J.rows[0].cells.length,F);
F=Math.max(1,F);
F-=1;
H=(J.tHead&&J.tHead.rows.length>0)?$(J.tHead.rows[J.tHead.rows.length-1].cells[F]):$(J.rows[0].cells[F])
}else{H=$(F);
J=J?$(J):H.up("table");
F=TableKit.getCellIndex(H)
}var E=TableKit.option("noSortClass descendingClass ascendingClass",J.id);
if(H.hasClassName(E.noSortClass)){return 
}B=B?B:(H.hasClassName(E.descendingClass)?1:-1);
var K=TableKit.getBodyRows(J);
if(H.hasClassName(E.ascendingClass)||H.hasClassName(E.descendingClass)){K.reverse()
}else{var D=TableKit.Sortable.getDataType(H,F,J);
var I=TableKit.Sortable.types;
K.sort(function(M,L){return B*I[D].compare(TableKit.getCellText(M.cells[F]),TableKit.getCellText(L.cells[F]))
})
}var C=J.tBodies[0];
var A=TableKit.Rows;
K.each(function(M,L){C.appendChild(M)
});
TableKit.Rows.stripe(J);
var G=TableKit.getHeaderCells(null,H);
$A(G).each(function(M,L){M=$(M);
M.removeClassName(E.ascendingClass);
M.removeClassName(E.descendingClass);
if(F===L){if(B===1){M.removeClassName(E.descendingClass);
M.addClassName(E.ascendingClass)
}else{M.removeClassName(E.ascendingClass);
M.addClassName(E.descendingClass)
}}})
},types:{},detectors:[],addSortType:function(){$A(arguments).each(function(A){TableKit.Sortable.types[A.name]=A
})
},getDataType:function(A,C,E){A=$(A);
C=(C||C===0)?C:TableKit.getCellIndex(A);
var G=TableKit.Sortable._coltypecache;
var B=G[E.id]?G[E.id]:(G[E.id]={});
if(!B[C]){var D="";
if(A.id&&TableKit.Sortable.types[A.id]){D=A.id
}D=A.classNames().detect(function(H){return(TableKit.Sortable.types[H])?true:false
});
if(!D){var F=TableKit.getBodyRows(E);
A=F[0].cells[C];
D=TableKit.Sortable.detectors.detect(function(H){return TableKit.Sortable.types[H].detect(TableKit.getCellText(A))
})
}B[C]=D
}return B[C]
},_coltypecache:{}};
TableKit.Sortable.detectors=$A($w("date-iso date date-eu date-au time currency datasize number text"));
TableKit.Sortable.Type=Class.create();
TableKit.Sortable.Type.prototype={initialize:function(B,A){this.name=B;
A=Object.extend({normal:function(C){return C
},pattern:/.*/},A||{});
this.normal=A.normal;
this.pattern=A.pattern;
if(A.compare){this.compare=A.compare
}if(A.detect){this.detect=A.detect
}},compare:function(B,A){return TableKit.Sortable.Type.compare(this.normal(B),this.normal(A))
},detect:function(A){return this.pattern.test(A)
}};
TableKit.Sortable.Type.compare=function(B,A){return B<A?-1:B===A?0:1
};
TableKit.Sortable.addSortType(new TableKit.Sortable.Type("number",{pattern:/^[-+]?[\d]*\.?[\d]+(?:[eE][-+]?[\d]+)?/,normal:function(A){A=parseFloat(A.replace(/^.*?([-+]?[\d]*\.?[\d]+(?:[eE][-+]?[\d]+)?).*$/,"$1"));
return isNaN(A)?0:A
}}),new TableKit.Sortable.Type("text",{normal:function(A){return A?A.toLowerCase():""
}}),new TableKit.Sortable.Type("casesensitivetext",{pattern:/^[A-Z]+$/}),new TableKit.Sortable.Type("datasize",{pattern:/^[-+]?[\d]*\.?[\d]+(?:[eE][-+]?[\d]+)?\s?[k|m|g|t]b$/i,normal:function(D){var E=D.match(/^([-+]?[\d]*\.?[\d]+([eE][-+]?[\d]+)?)\s?([k|m|g|t]?b)?/i);
var C=E[1]?Number(E[1]).valueOf():0;
var B=E[3]?E[3].substr(0,1).toLowerCase():"";
var A=C;
switch(B){case"k":A=C*1024;
break;
case"m":A=C*1024*1024;
break;
case"g":A=C*1024*1024*1024;
break;
case"t":A=C*1024*1024*1024*1024;
break
}return A
}}),new TableKit.Sortable.Type("date-au",{pattern:/^\d{2}\/\d{2}\/\d{4}\s?(?:\d{1,2}\:\d{2}(?:\:\d{2})?\s?[a|p]?m?)?/i,normal:function(C){if(!this.pattern.test(C)){return 0
}var F=C.match(/^(\d{2})\/(\d{2})\/(\d{4})\s?(?:(\d{1,2})\:(\d{2})(?:\:(\d{2}))?\s?([a|p]?m?))?/i);
var G=F[3];
var B=parseInt(F[2],10)-1;
var E=F[1];
var D=F[4]?F[4]:0;
if(F[7]&&F[7].toLowerCase().indexOf("p")!==-1){D=parseInt(F[4],10)+12
}var A=F[5]?F[5]:0;
var H=F[6]?F[6]:0;
return new Date(G,B,E,D,A,H,0).valueOf()
}}),new TableKit.Sortable.Type("date-us",{pattern:/^\d{2}\/\d{2}\/\d{4}\s?(?:\d{1,2}\:\d{2}(?:\:\d{2})?\s?[a|p]?m?)?/i,normal:function(C){if(!this.pattern.test(C)){return 0
}var F=C.match(/^(\d{2})\/(\d{2})\/(\d{4})\s?(?:(\d{1,2})\:(\d{2})(?:\:(\d{2}))?\s?([a|p]?m?))?/i);
var G=F[3];
var B=parseInt(F[1],10)-1;
var E=F[2];
var D=F[4]?F[4]:0;
if(F[7]&&F[7].toLowerCase().indexOf("p")!==-1){D=parseInt(F[4],10)+12
}var A=F[5]?F[5]:0;
var H=F[6]?F[6]:0;
return new Date(G,B,E,D,A,H,0).valueOf()
}}),new TableKit.Sortable.Type("date-eu",{pattern:/^\d{2}-\d{2}-\d{4}/i,normal:function(B){if(!this.pattern.test(B)){return 0
}var D=B.match(/^(\d{2})-(\d{2})-(\d{4})/);
var E=D[3];
var A=parseInt(D[2],10)-1;
var C=D[1];
return new Date(E,A,C).valueOf()
}}),new TableKit.Sortable.Type("date-iso",{pattern:/[\d]{4}-[\d]{2}-[\d]{2}(?:T[\d]{2}\:[\d]{2}(?:\:[\d]{2}(?:\.[\d]+)?)?(Z|([-+][\d]{2}:[\d]{2})?)?)?/,normal:function(A){if(!this.pattern.test(A)){return 0
}var E=A.match(/([\d]{4})(-([\d]{2})(-([\d]{2})(T([\d]{2}):([\d]{2})(:([\d]{2})(\.([\d]+))?)?(Z|(([-+])([\d]{2}):([\d]{2})))?)?)?)?/);
var D=0;
var B=new Date(E[1],0,1);
if(E[3]){B.setMonth(E[3]-1)
}if(E[5]){B.setDate(E[5])
}if(E[7]){B.setHours(E[7])
}if(E[8]){B.setMinutes(E[8])
}if(E[10]){B.setSeconds(E[10])
}if(E[12]){B.setMilliseconds(Number("0."+E[12])*1000)
}if(E[14]){D=(Number(E[16])*60)+Number(E[17]);
D*=((E[15]==="-")?1:-1)
}D-=B.getTimezoneOffset();
if(D!==0){var C=(Number(B)+(D*60*1000));
B.setTime(Number(C))
}return B.valueOf()
}}),new TableKit.Sortable.Type("date",{pattern:/^(?:sun|mon|tue|wed|thu|fri|sat)\,\s\d{1,2}\s(?:jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)\s\d{4}(?:\s\d{2}\:\d{2}(?:\:\d{2})?(?:\sGMT(?:[+-]\d{4})?)?)?/i,compare:function(B,A){if(B&&A){return TableKit.Sortable.Type.compare(new Date(B),new Date(A))
}else{return TableKit.Sortable.Type.compare(B?1:0,A?1:0)
}}}),new TableKit.Sortable.Type("time",{pattern:/^\d{1,2}\:\d{2}(?:\:\d{2})?(?:\s[a|p]m)?$/i,compare:function(B,A){var D=new Date();
var C=D.getMonth()+"/"+D.getDate()+"/"+D.getFullYear()+" ";
return TableKit.Sortable.Type.compare(new Date(C+B),new Date(C+A))
}}),new TableKit.Sortable.Type("currency",{pattern:/^[$����]/,normal:function(A){return A?parseFloat(A.replace(/[^-\d\.]/g,"")):0
}}));
TableKit.Resizable={init:function(D,B){var C=$(D);
if(C.tagName!=="TABLE"){return 
}TableKit.register(C,Object.extend(B||{},{resizable:true}));
var A=TableKit.getHeaderCells(C);
A.each(function(E){E=$(E);
Event.observe(E,"mouseover",TableKit.Resizable.initDetect);
Event.observe(E,"mouseout",TableKit.Resizable.killDetect)
})
},resize:function(D,C,B){var A;
if(typeof C==="number"){if(!D||(D.tagName&&D.tagName!=="TABLE")){return 
}D=$(D);
C=Math.min(D.rows[0].cells.length,C);
C=Math.max(1,C);
C-=1;
A=(D.tHead&&D.tHead.rows.length>0)?$(D.tHead.rows[D.tHead.rows.length-1].cells[C]):$(D.rows[0].cells[C])
}else{A=$(C);
D=D?$(D):A.up("table");
C=TableKit.getCellIndex(A)
}var E=parseInt(A.getStyle("paddingLeft"),10)+parseInt(A.getStyle("paddingRight"),10);
B=Math.max(B-E,TableKit.option("minWidth",D.id)[0]);
A.setStyle({width:B+"px"})
},initDetect:function(B){B=TableKit.e(B);
var A=Event.element(B);
Event.observe(A,"mousemove",TableKit.Resizable.detectHandle);
Event.observe(A,"mousedown",TableKit.Resizable.startResize)
},detectHandle:function(B){B=TableKit.e(B);
var A=Event.element(B);
if(TableKit.Resizable.pointerPos(A,Event.pointerX(B),Event.pointerY(B))){A.addClassName(TableKit.option("resizeOnHandleClass",A.up("table").id)[0]);
TableKit.Resizable._onHandle=true
}else{A.removeClassName(TableKit.option("resizeOnHandleClass",A.up("table").id)[0]);
TableKit.Resizable._onHandle=false
}},killDetect:function(B){B=TableKit.e(B);
TableKit.Resizable._onHandle=false;
var A=Event.element(B);
Event.stopObserving(A,"mousemove",TableKit.Resizable.detectHandle);
Event.stopObserving(A,"mousedown",TableKit.Resizable.startResize);
A.removeClassName(TableKit.option("resizeOnHandleClass",A.up("table").id)[0])
},startResize:function(C){C=TableKit.e(C);
if(!TableKit.Resizable._onHandle){return 
}var A=Event.element(C);
Event.stopObserving(A,"mousemove",TableKit.Resizable.detectHandle);
Event.stopObserving(A,"mousedown",TableKit.Resizable.startResize);
Event.stopObserving(A,"mouseout",TableKit.Resizable.killDetect);
TableKit.Resizable._cell=A;
var B=A.up("table");
TableKit.Resizable._tbl=B;
if(TableKit.option("showHandle",B.id)[0]){TableKit.Resizable._handle=$(document.createElement("div")).addClassName("resize-handle").setStyle({top:Position.cumulativeOffset(A)[1]+"px",left:Event.pointerX(C)+"px",height:B.getDimensions().height+"px"});
document.body.appendChild(TableKit.Resizable._handle)
}Event.observe(document,"mousemove",TableKit.Resizable.drag);
Event.observe(document,"mouseup",TableKit.Resizable.endResize);
Event.stop(C)
},endResize:function(B){B=TableKit.e(B);
var A=TableKit.Resizable._cell;
TableKit.Resizable.resize(null,A,(Event.pointerX(B)-Position.cumulativeOffset(A)[0]));
Event.stopObserving(document,"mousemove",TableKit.Resizable.drag);
Event.stopObserving(document,"mouseup",TableKit.Resizable.endResize);
if(TableKit.option("showHandle",TableKit.Resizable._tbl.id)[0]){$$("div.resize-handle").each(function(C){document.body.removeChild(C)
})
}Event.observe(A,"mouseout",TableKit.Resizable.killDetect);
TableKit.Resizable._tbl=TableKit.Resizable._handle=TableKit.Resizable._cell=null;
Event.stop(B)
},drag:function(A){A=TableKit.e(A);
if(TableKit.Resizable._handle===null){try{TableKit.Resizable.resize(TableKit.Resizable._tbl,TableKit.Resizable._cell,(Event.pointerX(A)-Position.cumulativeOffset(TableKit.Resizable._cell)[0]))
}catch(A){}}else{TableKit.Resizable._handle.setStyle({left:Event.pointerX(A)+"px"})
}return false
},pointerPos:function(B,A,D){var C=Position.cumulativeOffset(B);
return(D>=C[1]&&D<C[1]+B.offsetHeight&&A>=C[0]+B.offsetWidth-5&&A<C[0]+B.offsetWidth)
},_onHandle:false,_cell:null,_tbl:null,_handle:null};
TableKit.Editable={init:function(C,A){var B=$(C);
if(B.tagName!=="TABLE"){return 
}TableKit.register(B,Object.extend(A||{},{editable:true}));
Event.observe(B.tBodies[0],"click",TableKit.Editable._editCell)
},_editCell:function(B){B=TableKit.e(B);
var A=Event.findElement(B,"td");
TableKit.Editable.editCell(null,A)
},editCell:function(H,D,I){var G,J;
if(typeof D==="number"){if(!H||(H.tagName&&H.tagName!=="TABLE")){return 
}H=$(H);
D=Math.min(H.tBodies[0].rows.length,D);
D=Math.max(1,D);
D-=1;
I=Math.min(H.rows[0].cells.length,I);
I=Math.max(1,I);
I-=1;
J=$(H.tBodies[0].rows[D]);
G=$(J.cells[I])
}else{G=$(D);
H=(H&&H.tagName&&H.tagName!=="TABLE")?$(H):G.up("table");
J=G.up("tr")
}var C=TableKit.option("noEditClass",H.id);
if(G.hasClassName(C.noEditClass)){return 
}var E=$(TableKit.getHeaderCells(H,G)[TableKit.getCellIndex(G)]);
if(E.hasClassName(C.noEditClass)){return 
}TableKit.registerCell(G);
var B=TableKit.cells[G.id];
if(B.active){return 
}B.htmlContent=G.innerHTML;
var F=TableKit.Editable.types["text-input"];
if(E.id&&TableKit.Editable.types[E.id]){F=TableKit.Editable.types[E.id]
}else{var A=E.classNames().detect(function(K){return(TableKit.Editable.types[K])?true:false
});
F=A?TableKit.Editable.types[A]:F
}F.edit(G);
B.active=true
},types:{},addCellEditor:function(A){if(A&&A.name){TableKit.Editable.types[A.name]=A
}}};
TableKit.Editable.CellEditor=Class.create();
TableKit.Editable.CellEditor.prototype={initialize:function(B,A){this.name=B;
this.options=Object.extend({element:"input",attributes:{name:"value",type:"text"},selectOptions:[],showSubmit:true,submitText:"OK",showCancel:true,cancelText:"Cancel",ajaxURI:null,ajaxOptions:null},A||{})
},edit:function(B){B=$(B);
var H=this.options;
var E=B.up("table");
var D=$(document.createElement("form"));
D.id=B.id+"-form";
D.addClassName(TableKit.option("formClassName",E.id)[0]);
D.onsubmit=this._submit.bindAsEventListener(this);
var F=document.createElement(H.element);
$H(H.attributes).each(function(I){F[I.key]=I.value
});
switch(H.element){case"input":case"textarea":F.value=TableKit.getCellText(B);
break;
case"select":var A=TableKit.getCellText(B);
$A(H.selectOptions).each(function(I){F.options[F.options.length]=new Option(I[0],I[1]);
if(A===I[1]){F.options[F.options.length-1].selected="selected"
}});
break
}D.appendChild(F);
if(H.element==="textarea"){D.appendChild(document.createElement("br"))
}if(H.showSubmit){var G=document.createElement("input");
G.type="submit";
G.value=H.submitText;
G.className="editor_ok_button";
D.appendChild(G)
}if(H.showCancel){var C=document.createElement("a");
C.href="#";
C.appendChild(document.createTextNode(H.cancelText));
C.onclick=this._cancel.bindAsEventListener(this);
C.className="editor_cancel";
D.appendChild(C)
}B.innerHTML="";
B.appendChild(D)
},_submit:function(C){var A=Event.findElement(C,"td");
var B=Event.findElement(C,"form");
Event.stop(C);
this.submit(A,B)
},submit:function(A,E){var G=this.options;
E=E?E:A.down("form");
var B=$(TableKit.getHeaderCells(null,A)[TableKit.getCellIndex(A)]);
var F=A.up("tr");
var D=A.up("table");
var C="&row="+(TableKit.getRowIndex(F)+1)+"&cell="+(TableKit.getCellIndex(A)+1)+"&id="+F.id+"&field="+B.id+"&"+Form.serialize(E);
this.ajax=new Ajax.Updater(A,G.ajaxURI||TableKit.option("editAjaxURI",D.id)[0],Object.extend(G.ajaxOptions||TableKit.option("editAjaxOptions",D.id)[0],{postBody:C,onComplete:function(){var H=TableKit.cells[A.id];
H.active=false;
H.refresh=true
}}))
},_cancel:function(B){var A=Event.findElement(B,"td");
Event.stop(B);
this.cancel(A)
},cancel:function(A){this.ajax=null;
var B=TableKit.cells[A.id];
A.innerHTML=B.htmlContent;
B.htmlContent="";
B.active=false
},ajax:null};
TableKit.Editable.textInput=function(B,A){TableKit.Editable.addCellEditor(new TableKit.Editable.CellEditor(B,{element:"input",attributes:Object.extend({name:"value",type:"text"},A||{})}))
};
TableKit.Editable.textInput("text-input");
TableKit.Editable.multiLineInput=function(B,A){TableKit.Editable.addCellEditor(new TableKit.Editable.CellEditor(B,{element:"textarea",attributes:Object.extend({name:"value",rows:"5",cols:"20"},A||{})}))
};
TableKit.Editable.multiLineInput("multi-line-input");
TableKit.Editable.selectInput=function(C,B,A){TableKit.Editable.addCellEditor(new TableKit.Editable.CellEditor(C,{element:"select",attributes:Object.extend({name:"value"},B||{}),selectOptions:A}))
};
if(window.FastInit){FastInit.addOnLoad(TableKit.load)
}else{Event.observe(window,"load",TableKit.load)
};if(!Array.prototype.map){Array.prototype.map=function(C,D){var E=this.length;
var A=new Array(E);
for(var B=0;
B<E;
B++){if(B in this){A[B]=C.call(D,this[B],B,this)
}}return A
}
}if(!Array.prototype.filter){Array.prototype.filter=function(D,E){var F=this.length;
var A=new Array();
for(var C=0;
C<F;
C++){if(C in this){var B=this[C];
if(D.call(E,B,C,this)){A.push(B)
}}}return A
}
}if(!Array.prototype.forEach){Array.prototype.forEach=function(B,C){var D=this.length>>>0;
for(var A=0;
A<D;
A++){if(A in this){B.call(C,this[A],A,this)
}}}
}if(!Array.prototype.reduce){Array.prototype.reduce=function(D,B){var A=this.length;
if(!A&&(arguments.length==1)){throw new Error("reduce: empty array, no initial value")
}var C=0;
if(arguments.length<2){while(true){if(C in this){B=this[C++];
break
}if(++C>=A){throw new Error("reduce: no values, no initial value")
}}}for(;
C<A;
C++){if(C in this){B=D(B,this[C],C,this)
}}return B
}
}var pv={};
pv.version="3.3.1";
pv.identity=function(A){return A
};
pv.index=function(){return this.index
};
pv.child=function(){return this.childIndex
};
pv.parent=function(){return this.parent.index
};
pv.extend=function(B){function A(){}A.prototype=B.prototype||B;
return new A()
};
try{eval("pv.parse = function(x) x;")
}catch(e){pv.parse=function(A){var H=new RegExp("function\\s*(\\b\\w+)?\\s*\\([^)]*\\)\\s*","mg"),C,F,E=0,I="";
while(C=H.exec(A)){var D=C.index+C[0].length;
if(A.charAt(D)!="{"){I+=A.substring(E,D)+"{return ";
E=D;
for(var B=0;
B>=0&&D<A.length;
D++){var G=A.charAt(D);
switch(G){case'"':case"'":while(++D<A.length&&(F=A.charAt(D))!=G){if(F=="\\"){D++
}}break;
case"[":case"(":B++;
break;
case"]":case")":B--;
break;
case";":case",":if(B==0){B--
}break
}}I+=pv.parse(A.substring(E,--D))+";}";
E=D
}H.lastIndex=D
}I+=A.substring(E);
return I
}
}pv.css=function(B,A){return window.getComputedStyle?window.getComputedStyle(B,null).getPropertyValue(A):B.currentStyle[A]
};
pv.error=function(A){(typeof console=="undefined")?alert(A):console.error(A)
};
pv.listen=function(C,A,B){B=pv.listener(B);
return C.addEventListener?C.addEventListener(A,B,false):C.attachEvent("on"+A,B)
};
pv.listener=function(A){return A.$listener||(A.$listener=function(B){try{pv.event=B;
return A.call(this,B)
}finally{delete pv.event
}})
};
pv.ancestor=function(A,B){while(B){if(B==A){return true
}B=B.parentNode
}return false
};
pv.id=function(){var A=1;
return function(){return A++
}
}();
pv.functor=function(A){return typeof A=="function"?A:function(){return A
}
};
pv.listen(window,"load",function(){pv.$={i:0,x:document.getElementsByTagName("script")};
for(;
pv.$.i<pv.$.x.length;
pv.$.i++){pv.$.s=pv.$.x[pv.$.i];
if(pv.$.s.type=="text/javascript+protovis"){try{window.eval(pv.parse(pv.$.s.text))
}catch(e){pv.error(e)
}}}delete pv.$
});
pv.Format={};
pv.Format.re=function(A){return A.replace(/[\\\^\$\*\+\?\[\]\(\)\.\{\}]/g,"\\$&")
};
pv.Format.pad=function(D,C,B){var A=C-String(B).length;
return(A<1)?B:new Array(A+1).join(D)+B
};
pv.Format.date=function(A){var C=pv.Format.pad;
function B(D){return A.replace(/%[a-zA-Z0-9]/g,function(G){switch(G){case"%a":return["Sun","Mon","Tue","Wed","Thu","Fri","Sat"][D.getDay()];
case"%A":return["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"][D.getDay()];
case"%h":case"%b":return["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"][D.getMonth()];
case"%B":return["January","February","March","April","May","June","July","August","September","October","November","December"][D.getMonth()];
case"%c":return D.toLocaleString();
case"%C":return C("0",2,Math.floor(D.getFullYear()/100)%100);
case"%d":return C("0",2,D.getDate());
case"%x":case"%D":return C("0",2,D.getMonth()+1)+"/"+C("0",2,D.getDate())+"/"+C("0",2,D.getFullYear()%100);
case"%e":return C(" ",2,D.getDate());
case"%H":return C("0",2,D.getHours());
case"%I":var F=D.getHours()%12;
return F?C("0",2,F):12;
case"%m":return C("0",2,D.getMonth()+1);
case"%M":return C("0",2,D.getMinutes());
case"%n":return"\n";
case"%p":return D.getHours()<12?"AM":"PM";
case"%T":case"%X":case"%r":var F=D.getHours()%12;
return(F?C("0",2,F):12)+":"+C("0",2,D.getMinutes())+":"+C("0",2,D.getSeconds())+" "+(D.getHours()<12?"AM":"PM");
case"%R":return C("0",2,D.getHours())+":"+C("0",2,D.getMinutes());
case"%S":return C("0",2,D.getSeconds());
case"%Q":return C("0",3,D.getMilliseconds());
case"%t":return"\t";
case"%u":var E=D.getDay();
return E?E:1;
case"%w":return D.getDay();
case"%y":return C("0",2,D.getFullYear()%100);
case"%Y":return D.getFullYear();
case"%%":return"%"
}return G
})
}B.format=B;
B.parse=function(M){var K=1970,J=0,E=1,G=0,F=0,D=0;
var I=[function(){}];
var L=pv.Format.re(A).replace(/%[a-zA-Z0-9]/g,function(N){switch(N){case"%b":I.push(function(O){J={Jan:0,Feb:1,Mar:2,Apr:3,May:4,Jun:5,Jul:6,Aug:7,Sep:8,Oct:9,Nov:10,Dec:11}[O]
});
return"([A-Za-z]+)";
case"%h":case"%B":I.push(function(O){J={January:0,February:1,March:2,April:3,May:4,June:5,July:6,August:7,September:8,October:9,November:10,December:11}[O]
});
return"([A-Za-z]+)";
case"%e":case"%d":I.push(function(O){E=O
});
return"([0-9]+)";
case"%I":case"%H":I.push(function(O){G=O
});
return"([0-9]+)";
case"%m":I.push(function(O){J=O-1
});
return"([0-9]+)";
case"%M":I.push(function(O){F=O
});
return"([0-9]+)";
case"%p":I.push(function(O){if(G==12){if(O=="am"){G=0
}}else{if(O=="pm"){G=Number(G)+12
}}});
return"(am|pm)";
case"%S":I.push(function(O){D=O
});
return"([0-9]+)";
case"%y":I.push(function(O){O=Number(O);
K=O+(((0<=O)&&(O<69))?2000:(((O>=69)&&(O<100)?1900:0)))
});
return"([0-9]+)";
case"%Y":I.push(function(O){K=O
});
return"([0-9]+)";
case"%%":I.push(function(){});
return"%"
}return N
});
var H=M.match(L);
if(H){H.forEach(function(N,O){I[O](N)
})
}return new Date(K,J,E,G,F,D)
};
return B
};
pv.Format.time=function(A){var C=pv.Format.pad;
function B(F){F=Number(F);
switch(A){case"short":if(F>=31536000000){return(F/31536000000).toFixed(1)+" years"
}else{if(F>=604800000){return(F/604800000).toFixed(1)+" weeks"
}else{if(F>=86400000){return(F/86400000).toFixed(1)+" days"
}else{if(F>=3600000){return(F/3600000).toFixed(1)+" hours"
}else{if(F>=60000){return(F/60000).toFixed(1)+" minutes"
}}}}}return(F/1000).toFixed(1)+" seconds";
case"long":var E=[],H=((F%60000)/1000)>>0,D=((F%3600000)/60000)>>0;
E.push(C("0",2,H));
if(F>=3600000){var G=((F%86400000)/3600000)>>0;
E.push(C("0",2,D));
if(F>=86400000){E.push(C("0",2,G));
E.push(Math.floor(F/86400000).toFixed())
}else{E.push(G.toFixed())
}}else{E.push(D.toFixed())
}return E.reverse().join(":")
}}B.format=B;
B.parse=function(H){switch(A){case"short":var G=/([0-9,.]+)\s*([a-z]+)/g,D,F=0;
while(D=G.exec(H)){var I=parseFloat(D[0].replace(",","")),E=0;
switch(D[2].toLowerCase()){case"year":case"years":E=31536000000;
break;
case"week":case"weeks":E=604800000;
break;
case"day":case"days":E=86400000;
break;
case"hour":case"hours":E=3600000;
break;
case"minute":case"minutes":E=60000;
break;
case"second":case"seconds":E=1000;
break
}F+=I*E
}return F;
case"long":var D=H.replace(",","").split(":").reverse(),F=0;
if(D.length){F+=parseFloat(D[0])*1000
}if(D.length>1){F+=parseFloat(D[1])*60000
}if(D.length>2){F+=parseFloat(D[2])*3600000
}if(D.length>3){F+=parseFloat(D[3])*86400000
}return F
}};
return B
};
pv.Format.number=function(){var G=0,C=Infinity,A=0,J=0,E=0,B=1,F="0",I="0",H=true,D=".",N=",",M="\u2212",K="";
function L(O){if(Infinity>E){O=Math.round(O*B)/B
}var Q=String(Math.abs(O)).split(".");
var P=Q[0];
if(P.length>C){P=P.substring(P.length-C)
}if(H&&(P.length<G)){P=new Array(G-P.length+1).join(F)+P
}if(P.length>3){P=P.replace(/\B(?=(?:\d{3})+(?!\d))/g,N)
}if(!H&&(P.length<A)){P=new Array(A-P.length+1).join(F)+P
}Q[0]=O<0?M+P+K:P;
var R=Q[1]||"";
if(R.length<J){Q[1]=R+new Array(J-R.length+1).join(I)
}return Q.join(D)
}L.format=L;
L.parse=function(O){var R=pv.Format.re;
var Q=String(O).replace(new RegExp("^("+R(F)+")*"),"").replace(new RegExp("("+R(I)+")*$"),"").split(D);
var P=Q[0].replace(new RegExp(R(N),"g"),"");
if(P.length>C){P=P.substring(P.length-C)
}var S=Q[1]?Number("0."+Q[1]):0;
if(Infinity>E){S=Math.round(S*B)/B
}return Math.round(P)+S
};
L.integerDigits=function(P,O){if(arguments.length){G=Number(P);
C=(arguments.length>1)?Number(O):G;
A=G+Math.floor(G/3)*N.length;
return this
}return[G,C]
};
L.fractionDigits=function(P,O){if(arguments.length){J=Number(P);
E=(arguments.length>1)?Number(O):J;
B=Math.pow(10,E);
return this
}return[J,E]
};
L.integerPad=function(O){if(arguments.length){F=String(O);
H=/\d/.test(F);
return this
}return F
};
L.fractionPad=function(O){if(arguments.length){I=String(O);
return this
}return I
};
L.decimal=function(O){if(arguments.length){D=String(O);
return this
}return D
};
L.group=function(O){if(arguments.length){N=O?String(O):"";
A=G+Math.floor(G/3)*N.length;
return this
}return N
};
L.negativeAffix=function(O,P){if(arguments.length){M=String(O||"");
K=String(P||"");
return this
}return[M,K]
};
return L
};
pv.map=function(C,A){var B={};
return A?C.map(function(E,D){B.index=D;
return A.call(B,E)
}):C.slice()
};
pv.repeat=function(B,A){if(arguments.length==1){A=2
}return pv.blend(pv.range(A).map(function(){return B
}))
};
pv.cross=function(D,C){var H=[];
for(var F=0,G=D.length,B=C.length;
F<G;
F++){for(var E=0,A=D[F];
E<B;
E++){H.push([A,C[E]])
}}return H
};
pv.blend=function(A){return Array.prototype.concat.apply([],A)
};
pv.transpose=function(E){var F=E.length,A=pv.max(E,function(G){return G.length
});
if(A>F){E.length=A;
for(var D=F;
D<A;
D++){E[D]=new Array(F)
}for(var D=0;
D<F;
D++){for(var B=D+1;
B<A;
B++){var C=E[D][B];
E[D][B]=E[B][D];
E[B][D]=C
}}}else{for(var D=0;
D<A;
D++){E[D].length=F
}for(var D=0;
D<F;
D++){for(var B=0;
B<D;
B++){var C=E[D][B];
E[D][B]=E[B][D];
E[B][D]=C
}}}E.length=A;
for(var D=0;
D<A;
D++){E[D].length=F
}return E
};
pv.normalize=function(E,D){var B=pv.map(E,D),C=pv.sum(B);
for(var A=0;
A<B.length;
A++){B[A]/=C
}return B
};
pv.permute=function(E,A,B){if(!B){B=pv.identity
}var C=new Array(A.length),D={};
A.forEach(function(F,G){D.index=F;
C[G]=B.call(D,E[F])
});
return C
};
pv.numerate=function(A,B){if(!B){B=pv.identity
}var C={},D={};
A.forEach(function(E,F){D.index=F;
C[B.call(D,E)]=F
});
return C
};
pv.uniq=function(F,B){if(!B){B=pv.identity
}var C={},A=[],D={},E;
F.forEach(function(G,H){D.index=H;
E=B.call(D,G);
if(!(E in C)){C[E]=A.push(E)
}});
return A
};
pv.naturalOrder=function(B,A){return(B<A)?-1:((B>A)?1:0)
};
pv.reverseOrder=function(A,B){return(B<A)?-1:((B>A)?1:0)
};
pv.search=function(G,F,E){if(!E){E=pv.identity
}var A=0,D=G.length-1;
while(A<=D){var B=(A+D)>>1,C=E(G[B]);
if(C<F){A=B+1
}else{if(C>F){D=B-1
}else{return B
}}}return -A-1
};
pv.search.index=function(D,C,B){var A=pv.search(D,C,B);
return(A<0)?(-A-1):A
};
pv.range=function(F,C,D){if(arguments.length==1){C=F;
F=0
}if(D==undefined){D=1
}if((C-F)/D==Infinity){throw new Error("range must be finite")
}var E=[],B=0,A;
C-=(C-F)*1e-10;
if(D<0){while((A=F+D*B++)>C){E.push(A)
}}else{while((A=F+D*B++)<C){E.push(A)
}}return E
};
pv.random=function(C,A,B){if(arguments.length==1){A=C;
C=0
}if(B==undefined){B=1
}return B?(Math.floor(Math.random()*(A-C)/B)*B+C):(Math.random()*(A-C)+C)
};
pv.sum=function(C,A){var B={};
return C.reduce(A?function(E,F,D){B.index=D;
return E+A.call(B,F)
}:function(D,E){return D+E
},0)
};
pv.max=function(B,A){if(A==pv.index){return B.length-1
}return Math.max.apply(null,A?pv.map(B,A):B)
};
pv.max.index=function(G,D){if(!G.length){return -1
}if(D==pv.index){return G.length-1
}if(!D){D=pv.identity
}var B=0,F=-Infinity,E={};
for(var C=0;
C<G.length;
C++){E.index=C;
var A=D.call(E,G[C]);
if(A>F){F=A;
B=C
}}return B
};
pv.min=function(B,A){if(A==pv.index){return 0
}return Math.min.apply(null,A?pv.map(B,A):B)
};
pv.min.index=function(G,E){if(!G.length){return -1
}if(E==pv.index){return 0
}if(!E){E=pv.identity
}var D=0,B=Infinity,F={};
for(var C=0;
C<G.length;
C++){F.index=C;
var A=E.call(F,G[C]);
if(A<B){B=A;
D=C
}}return D
};
pv.mean=function(B,A){return pv.sum(B,A)/B.length
};
pv.median=function(C,B){if(B==pv.index){return(C.length-1)/2
}C=pv.map(C,B).sort(pv.naturalOrder);
if(C.length%2){return C[Math.floor(C.length/2)]
}var A=C.length/2;
return(C[A-1]+C[A])/2
};
pv.variance=function(G,D){if(G.length<1){return NaN
}if(G.length==1){return 0
}var A=pv.mean(G,D),C=0,F={};
if(!D){D=pv.identity
}for(var B=0;
B<G.length;
B++){F.index=B;
var E=D.call(F,G[B])-A;
C+=E*E
}return C
};
pv.deviation=function(B,A){return Math.sqrt(pv.variance(B,A)/(B.length-1))
};
pv.log=function(B,A){return Math.log(B)/Math.log(A)
};
pv.logSymmetric=function(B,A){return(B==0)?0:((B<0)?-pv.log(-B,A):pv.log(B,A))
};
pv.logAdjusted=function(B,A){if(!isFinite(B)){return B
}var C=B<0;
if(B<A){B+=(A-B)/A
}return C?-pv.log(B,A):pv.log(B,A)
};
pv.logFloor=function(B,A){return(B>0)?Math.pow(A,Math.floor(pv.log(B,A))):-Math.pow(A,-Math.floor(-pv.log(-B,A)))
};
pv.logCeil=function(B,A){return(B>0)?Math.pow(A,Math.ceil(pv.log(B,A))):-Math.pow(A,-Math.ceil(-pv.log(-B,A)))
};
(function(){var A=Math.PI/180,B=180/Math.PI;
pv.radians=function(C){return A*C
};
pv.degrees=function(C){return B*C
}
})();
pv.keys=function(B){var C=[];
for(var A in B){C.push(A)
}return C
};
pv.entries=function(B){var C=[];
for(var A in B){C.push({key:A,value:B[A]})
}return C
};
pv.values=function(B){var C=[];
for(var A in B){C.push(B[A])
}return C
};
pv.dict=function(D,E){var A={},F={};
for(var C=0;
C<D.length;
C++){if(C in D){var B=D[C];
F.index=C;
A[B]=E.call(F,B)
}}return A
};
pv.dom=function(A){return new pv.Dom(A)
};
pv.Dom=function(A){this.$map=A
};
pv.Dom.prototype.$leaf=function(A){return typeof A!="object"
};
pv.Dom.prototype.leaf=function(A){if(arguments.length){this.$leaf=A;
return this
}return this.$leaf
};
pv.Dom.prototype.root=function(D){var B=this.$leaf,A=C(this.$map);
function C(G){var H=new pv.Dom.Node();
for(var F in G){var E=G[F];
H.appendChild(B(E)?new pv.Dom.Node(E):C(E)).nodeName=F
}return H
}A.nodeName=D;
return A
};
pv.Dom.prototype.nodes=function(){return this.root().nodes()
};
pv.Dom.Node=function(A){this.nodeValue=A;
this.childNodes=[]
};
pv.Dom.Node.prototype.parentNode=null;
pv.Dom.Node.prototype.firstChild=null;
pv.Dom.Node.prototype.lastChild=null;
pv.Dom.Node.prototype.previousSibling=null;
pv.Dom.Node.prototype.nextSibling=null;
pv.Dom.Node.prototype.removeChild=function(B){var A=this.childNodes.indexOf(B);
if(A==-1){throw new Error("child not found")
}this.childNodes.splice(A,1);
if(B.previousSibling){B.previousSibling.nextSibling=B.nextSibling
}else{this.firstChild=B.nextSibling
}if(B.nextSibling){B.nextSibling.previousSibling=B.previousSibling
}else{this.lastChild=B.previousSibling
}delete B.nextSibling;
delete B.previousSibling;
delete B.parentNode;
return B
};
pv.Dom.Node.prototype.appendChild=function(A){if(A.parentNode){A.parentNode.removeChild(A)
}A.parentNode=this;
A.previousSibling=this.lastChild;
if(this.lastChild){this.lastChild.nextSibling=A
}else{this.firstChild=A
}this.lastChild=A;
this.childNodes.push(A);
return A
};
pv.Dom.Node.prototype.insertBefore=function(C,B){if(!B){return this.appendChild(C)
}var A=this.childNodes.indexOf(B);
if(A==-1){throw new Error("child not found")
}if(C.parentNode){C.parentNode.removeChild(C)
}C.parentNode=this;
C.nextSibling=B;
C.previousSibling=B.previousSibling;
if(B.previousSibling){B.previousSibling.nextSibling=C
}else{if(B==this.lastChild){this.lastChild=C
}this.firstChild=C
}this.childNodes.splice(A,0,C);
return C
};
pv.Dom.Node.prototype.replaceChild=function(C,B){var A=this.childNodes.indexOf(B);
if(A==-1){throw new Error("child not found")
}if(C.parentNode){C.parentNode.removeChild(C)
}C.parentNode=this;
C.nextSibling=B.nextSibling;
C.previousSibling=B.previousSibling;
if(B.previousSibling){B.previousSibling.nextSibling=C
}else{this.firstChild=C
}if(B.nextSibling){B.nextSibling.previousSibling=C
}else{this.lastChild=C
}this.childNodes[A]=C;
return B
};
pv.Dom.Node.prototype.visitBefore=function(B){function A(E,C){B(E,C);
for(var D=E.firstChild;
D;
D=D.nextSibling){A(D,C+1)
}}A(this,0)
};
pv.Dom.Node.prototype.visitAfter=function(B){function A(E,C){for(var D=E.firstChild;
D;
D=D.nextSibling){A(D,C+1)
}B(E,C)
}A(this,0)
};
pv.Dom.Node.prototype.sort=function(B){if(this.firstChild){this.childNodes.sort(B);
var C=this.firstChild=this.childNodes[0],D;
delete C.previousSibling;
for(var A=1;
A<this.childNodes.length;
A++){C.sort(B);
D=this.childNodes[A];
D.previousSibling=C;
C=C.nextSibling=D
}this.lastChild=C;
delete C.nextSibling;
C.sort(B)
}return this
};
pv.Dom.Node.prototype.reverse=function(){var A=[];
this.visitAfter(function(C){while(C.lastChild){A.push(C.removeChild(C.lastChild))
}for(var B;
B=A.pop();
){C.insertBefore(B,C.firstChild)
}});
return this
};
pv.Dom.Node.prototype.nodes=function(){var B=[];
function A(C){B.push(C);
C.childNodes.forEach(A)
}A(this,B);
return B
};
pv.Dom.Node.prototype.toggle=function(A){if(A){return this.toggled?this.visitBefore(function(D){if(D.toggled){D.toggle()
}}):this.visitAfter(function(D){if(!D.toggled){D.toggle()
}})
}var C=this;
if(C.toggled){for(var B;
B=C.toggled.pop();
){C.appendChild(B)
}delete C.toggled
}else{if(C.lastChild){C.toggled=[];
while(C.lastChild){C.toggled.push(C.removeChild(C.lastChild))
}}}};
pv.nodes=function(B){var A=new pv.Dom.Node();
for(var C=0;
C<B.length;
C++){A.appendChild(new pv.Dom.Node(B[C]))
}return A.nodes()
};
pv.tree=function(A){return new pv.Tree(A)
};
pv.Tree=function(A){this.array=A
};
pv.Tree.prototype.keys=function(A){this.k=A;
return this
};
pv.Tree.prototype.value=function(A){this.v=A;
return this
};
pv.Tree.prototype.map=function(){var F={},G={};
for(var B=0;
B<this.array.length;
B++){G.index=B;
var E=this.array[B],D=this.k.call(G,E),C=F;
for(var A=0;
A<D.length-1;
A++){C=C[D[A]]||(C[D[A]]={})
}C[D[A]]=this.v?this.v.call(G,E):E
}return F
};
pv.nest=function(A){return new pv.Nest(A)
};
pv.Nest=function(A){this.array=A;
this.keys=[]
};
pv.Nest.prototype.key=function(A){this.keys.push(A);
return this
};
pv.Nest.prototype.sortKeys=function(A){this.keys[this.keys.length-1].order=A||pv.naturalOrder;
return this
};
pv.Nest.prototype.sortValues=function(A){this.order=A||pv.naturalOrder;
return this
};
pv.Nest.prototype.map=function(){var H={},E=[];
for(var G,F=0;
F<this.array.length;
F++){var B=this.array[F];
var A=H;
for(G=0;
G<this.keys.length-1;
G++){var D=this.keys[G](B);
if(!A[D]){A[D]={}
}A=A[D]
}D=this.keys[G](B);
if(!A[D]){var C=[];
E.push(C);
A[D]=C
}A[D].push(B)
}if(this.order){for(var G=0;
G<E.length;
G++){E[G].sort(this.order)
}}return H
};
pv.Nest.prototype.entries=function(){function A(E){var F=[];
for(var D in E){var C=E[D];
F.push({key:D,values:(C instanceof Array)?C:A(C)})
}return F
}function B(F,D){var E=this.keys[D].order;
if(E){F.sort(function(H,G){return E(H.key,G.key)
})
}if(++D<this.keys.length){for(var C=0;
C<F.length;
C++){B.call(this,F[C].values,D)
}}return F
}return B.call(this,A(this.map()),0)
};
pv.Nest.prototype.rollup=function(B){function A(E){for(var C in E){var D=E[C];
if(D instanceof Array){E[C]=B(D)
}else{A(D)
}}return E
}return A(this.map())
};
pv.flatten=function(A){return new pv.Flatten(A)
};
pv.Flatten=function(A){this.map=A;
this.keys=[]
};
pv.Flatten.prototype.key=function(A,B){this.keys.push({name:A,value:B});
delete this.$leaf;
return this
};
pv.Flatten.prototype.leaf=function(A){this.keys.length=0;
this.$leaf=A;
return this
};
pv.Flatten.prototype.array=function(){var B=[],A=[],F=this.keys,C=this.$leaf;
if(C){function E(I,H){if(C(I)){B.push({keys:A.slice(),value:I})
}else{for(var G in I){A.push(G);
E(I[G],H+1);
A.pop()
}}}E(this.map,0);
return B
}function D(I,H){if(H<F.length-1){for(var G in I){A.push(G);
D(I[G],H+1);
A.pop()
}}else{B.push(A.concat(I))
}}D(this.map,0);
return B.map(function(H){var G={};
for(var K=0;
K<F.length;
K++){var J=F[K],I=H[K];
G[J.name]=J.value?J.value.call(null,I):I
}return G
})
};
pv.vector=function(A,B){return new pv.Vector(A,B)
};
pv.Vector=function(A,B){this.x=A;
this.y=B
};
pv.Vector.prototype.perp=function(){return new pv.Vector(-this.y,this.x)
};
pv.Vector.prototype.norm=function(){var A=this.length();
return this.times(A?(1/A):1)
};
pv.Vector.prototype.length=function(){return Math.sqrt(this.x*this.x+this.y*this.y)
};
pv.Vector.prototype.times=function(A){return new pv.Vector(this.x*A,this.y*A)
};
pv.Vector.prototype.plus=function(A,B){return(arguments.length==1)?new pv.Vector(this.x+A.x,this.y+A.y):new pv.Vector(this.x+A,this.y+B)
};
pv.Vector.prototype.minus=function(A,B){return(arguments.length==1)?new pv.Vector(this.x-A.x,this.y-A.y):new pv.Vector(this.x-A,this.y-B)
};
pv.Vector.prototype.dot=function(A,B){return(arguments.length==1)?this.x*A.x+this.y*A.y:this.x*A+this.y*B
};
pv.Transform=function(){};
pv.Transform.prototype={k:1,x:0,y:0};
pv.Transform.identity=new pv.Transform();
pv.Transform.prototype.translate=function(A,C){var B=new pv.Transform();
B.k=this.k;
B.x=this.k*A+this.x;
B.y=this.k*C+this.y;
return B
};
pv.Transform.prototype.scale=function(B){var A=new pv.Transform();
A.k=this.k*B;
A.x=this.x;
A.y=this.y;
return A
};
pv.Transform.prototype.invert=function(){var B=new pv.Transform(),A=1/this.k;
B.k=A;
B.x=-this.x*A;
B.y=-this.y*A;
return B
};
pv.Transform.prototype.times=function(A){var B=new pv.Transform();
B.k=this.k*A.k;
B.x=this.k*A.x+this.x;
B.y=this.k*A.y+this.y;
return B
};
pv.Scale=function(){};
pv.Scale.interpolator=function(B,A){if(typeof B=="number"){return function(C){return C*(A-B)+B
}
}B=pv.color(B).rgb();
A=pv.color(A).rgb();
return function(D){var C=B.a*(1-D)+A.a*D;
if(C<0.00001){C=0
}return(B.a==0)?pv.rgb(A.r,A.g,A.b,C):((A.a==0)?pv.rgb(B.r,B.g,B.b,C):pv.rgb(Math.round(B.r*(1-D)+A.r*D),Math.round(B.g*(1-D)+A.g*D),Math.round(B.b*(1-D)+A.b*D),C))
}
};
pv.Scale.quantitative=function(){var I=[0,1],E=[0,1],A=[0,1],F=[pv.identity],J=Number,B=false,H=pv.identity,G=pv.identity,D=String;
function K(L){return new Date(L)
}function C(L){var M=pv.search(I,L);
if(M<0){M=-M-2
}M=Math.max(0,Math.min(F.length-1,M));
return F[M]((H(L)-E[M])/(E[M+1]-E[M]))
}C.transform=function(M,L){H=function(N){return B?-M(-N):M(N)
};
G=function(N){return B?-L(-N):L(N)
};
E=I.map(H);
return this
};
C.domain=function(O,M,L){if(arguments.length){var N;
if(O instanceof Array){if(arguments.length<2){M=pv.identity
}if(arguments.length<3){L=M
}N=O.length&&M(O[0]);
I=O.length?[pv.min(O,M),pv.max(O,L)]:[]
}else{N=O;
I=Array.prototype.slice.call(arguments).map(Number)
}if(!I.length){I=[-Infinity,Infinity]
}else{if(I.length==1){I=[I[0],I[0]]
}}B=(I[0]||I[I.length-1])<0;
E=I.map(H);
J=(N instanceof Date)?K:Number;
return this
}return I.map(J)
};
C.range=function(){if(arguments.length){A=Array.prototype.slice.call(arguments);
if(!A.length){A=[-Infinity,Infinity]
}else{if(A.length==1){A=[A[0],A[0]]
}}F=[];
for(var L=0;
L<A.length-1;
L++){F.push(pv.Scale.interpolator(A[L],A[L+1]))
}return this
}return A
};
C.invert=function(M){var L=pv.search(A,M);
if(L<0){L=-L-2
}L=Math.max(0,Math.min(F.length-1,L));
return J(G(E[L]+(M-A[L])/(A[L+1]-A[L])*(E[L+1]-E[L])))
};
C.ticks=function(Q){var M=I[0],S=I[I.length-1],W=S<M,T=W?S:M,X=W?M:S,b=X-T;
if(!b||!isFinite(b)){if(J==K){D=pv.Format.date("%x")
}return[J(T)]
}if(J==K){function V(f,c){switch(c){case 31536000000:f.setMonth(0);
case 2592000000:f.setDate(1);
case 604800000:if(c==604800000){f.setDate(f.getDate()-f.getDay())
}case 86400000:f.setHours(0);
case 3600000:f.setMinutes(0);
case 60000:f.setSeconds(0);
case 1000:f.setMilliseconds(0)
}}var U,a,Z,O=1;
if(b>=3*31536000000){U=31536000000;
a="%Y";
Z=function(c){c.setFullYear(c.getFullYear()+O)
}
}else{if(b>=3*2592000000){U=2592000000;
a="%m/%Y";
Z=function(c){c.setMonth(c.getMonth()+O)
}
}else{if(b>=3*604800000){U=604800000;
a="%m/%d";
Z=function(c){c.setDate(c.getDate()+7*O)
}
}else{if(b>=3*86400000){U=86400000;
a="%m/%d";
Z=function(c){c.setDate(c.getDate()+O)
}
}else{if(b>=3*3600000){U=3600000;
a="%I:%M %p";
Z=function(c){c.setHours(c.getHours()+O)
}
}else{if(b>=3*60000){U=60000;
a="%I:%M %p";
Z=function(c){c.setMinutes(c.getMinutes()+O)
}
}else{if(b>=3*1000){U=1000;
a="%I:%M:%S";
Z=function(c){c.setSeconds(c.getSeconds()+O)
}
}else{U=1;
a="%S.%Qs";
Z=function(c){c.setTime(c.getTime()+O)
}
}}}}}}}D=pv.Format.date(a);
var P=new Date(T),L=[];
V(P,U);
var N=b/U;
if(N>10){switch(U){case 3600000:O=(N>20)?6:3;
P.setHours(Math.floor(P.getHours()/O)*O);
break;
case 2592000000:O=3;
P.setMonth(Math.floor(P.getMonth()/O)*O);
break;
case 60000:O=(N>30)?15:((N>15)?10:5);
P.setMinutes(Math.floor(P.getMinutes()/O)*O);
break;
case 1000:O=(N>90)?15:((N>60)?10:5);
P.setSeconds(Math.floor(P.getSeconds()/O)*O);
break;
case 1:O=(N>1000)?250:((N>200)?100:((N>100)?50:((N>50)?25:5)));
P.setMilliseconds(Math.floor(P.getMilliseconds()/O)*O);
break;
default:O=pv.logCeil(N/15,10);
if(N/O<2){O/=5
}else{if(N/O<5){O/=2
}}P.setFullYear(Math.floor(P.getFullYear()/O)*O);
break
}}while(true){Z(P);
if(P>X){break
}L.push(new Date(P))
}return W?L.reverse():L
}if(!arguments.length){Q=10
}var O=pv.logFloor(b/Q,10),R=Q/(b/O);
if(R<=0.15){O*=10
}else{if(R<=0.35){O*=5
}else{if(R<=0.75){O*=2
}}}var M=Math.ceil(T/O)*O,S=Math.floor(X/O)*O;
D=pv.Format.number().fractionDigits(Math.max(0,-Math.floor(pv.log(O,10)+0.01)));
var Y=pv.range(M,S+O,O);
return W?Y.reverse():Y
};
C.tickFormat=function(L){return D(L)
};
C.nice=function(){if(I.length!=2){return this
}var R=I[0],M=I[I.length-1],N=M<R,O=N?M:R,L=N?R:M,P=L-O;
if(!P||!isFinite(P)){return this
}var Q=Math.pow(10,Math.round(Math.log(P)/Math.log(10))-1);
I=[Math.floor(O/Q)*Q,Math.ceil(L/Q)*Q];
if(N){I.reverse()
}E=I.map(H);
return this
};
C.by=function(L){function M(){return C(L.apply(this,arguments))
}for(var N in C){M[N]=C[N]
}return M
};
C.domain.apply(C,arguments);
return C
};
pv.Scale.linear=function(){var A=pv.Scale.quantitative();
A.domain.apply(A,arguments);
return A
};
pv.Scale.log=function(){var E=pv.Scale.quantitative(1,10),A,D,B=function(F){return Math.log(F)/D
},C=function(F){return Math.pow(A,F)
};
E.ticks=function(){var J=E.domain(),K=J[0]<0,H=Math.floor(K?-B(-J[0]):B(J[0])),G=Math.ceil(K?-B(-J[1]):B(J[1])),I=[];
if(K){I.push(-C(-H));
for(;
H++<G;
){for(var F=A-1;
F>0;
F--){I.push(-C(-H)*F)
}}}else{for(;
H<G;
H++){for(var F=1;
F<A;
F++){I.push(C(H)*F)
}}I.push(C(H))
}for(H=0;
I[H]<J[0];
H++){}for(G=I.length;
I[G-1]>J[1];
G--){}return I.slice(H,G)
};
E.tickFormat=function(F){return F.toPrecision(1)
};
E.nice=function(){var F=E.domain();
return E.domain(pv.logFloor(F[0],A),pv.logCeil(F[1],A))
};
E.base=function(F){if(arguments.length){A=Number(F);
D=Math.log(A);
E.transform(B,C);
return this
}return A
};
E.domain.apply(E,arguments);
return E.base(10)
};
pv.Scale.root=function(){var A=pv.Scale.quantitative();
A.power=function(C){if(arguments.length){var B=Number(C),D=1/B;
A.transform(function(E){return Math.pow(E,D)
},function(E){return Math.pow(E,B)
});
return this
}return B
};
A.domain.apply(A,arguments);
return A.power(2)
};
pv.Scale.ordinal=function(){var E=[],A={},B=[],D=0;
function C(F){if(!(F in A)){A[F]=E.push(F)-1
}return B[A[F]%B.length]
}C.domain=function(J,H){if(arguments.length){J=(J instanceof Array)?((arguments.length>1)?pv.map(J,H):J):Array.prototype.slice.call(arguments);
E=[];
var F={};
for(var G=0;
G<J.length;
G++){var I=J[G];
if(!(I in F)){F[I]=true;
E.push(I)
}}A=pv.numerate(E);
return this
}return E
};
C.range=function(G,F){if(arguments.length){B=(G instanceof Array)?((arguments.length>1)?pv.map(G,F):G):Array.prototype.slice.call(arguments);
if(typeof B[0]=="string"){B=B.map(pv.color)
}return this
}return B
};
C.split=function(G,F){var H=(F-G)/this.domain().length;
B=pv.range(G+H/2,F,H);
return this
};
C.splitFlush=function(G,F){var I=this.domain().length,H=(F-G)/(I-1);
B=(I==1)?[(G+F)/2]:pv.range(G,F+H/2,H);
return this
};
C.splitBanded=function(G,F,L){if(arguments.length<3){L=1
}if(L<0){var M=this.domain().length,J=-L*M,H=F-G-J,K=H/(M+1);
B=pv.range(G+K,F,K-L);
B.band=-L
}else{var I=(F-G)/(this.domain().length+(1-L));
B=pv.range(G+I*(1-L),F,I);
B.band=I*L
}return this
};
C.by=function(F){function G(){return C(F.apply(this,arguments))
}for(var H in C){G[H]=C[H]
}return G
};
C.domain.apply(C,arguments);
return C
};
pv.Scale.quantile=function(){var F=-1,A=-1,B=[],D=[],E=pv.Scale.linear();
function C(G){return E(Math.max(0,Math.min(A,pv.search.index(B,G)-1))/A)
}C.quantiles=function(G){if(arguments.length){F=Number(G);
if(F<0){B=[D[0]].concat(D);
A=D.length-1
}else{B=[];
B[0]=D[0];
for(var H=1;
H<=F;
H++){B[H]=D[~~(H*(D.length-1)/F)]
}A=F-1
}return this
}return B
};
C.domain=function(H,G){if(arguments.length){D=(H instanceof Array)?pv.map(H,G):Array.prototype.slice.call(arguments);
D.sort(pv.naturalOrder);
C.quantiles(F);
return this
}return D
};
C.range=function(){if(arguments.length){E.range.apply(E,arguments);
return this
}return E.range()
};
C.by=function(G){function H(){return C(G.apply(this,arguments))
}for(var I in C){H[I]=C[I]
}return H
};
C.domain.apply(C,arguments);
return C
};
pv.histogram=function(B,A){var C=true;
return{bins:function(I){var D=pv.map(B,A),H=[];
if(!arguments.length){I=pv.Scale.linear(D).ticks()
}for(var G=0;
G<I.length-1;
G++){var F=H[G]=[];
F.x=I[G];
F.dx=I[G+1]-I[G];
F.y=0
}for(var G=0;
G<D.length;
G++){var E=pv.search.index(I,D[G])-1,F=H[Math.max(0,Math.min(H.length-1,E))];
F.y++;
F.push(B[G])
}if(!C){for(var G=0;
G<H.length;
G++){H[G].y/=D.length
}}return H
},frequency:function(D){if(arguments.length){C=Boolean(D);
return this
}return C
}}
};
pv.color=function(I){if(I.rgb){return I.rgb()
}var K=/([a-z]+)\((.*)\)/i.exec(I);
if(K){var J=K[2].split(","),H=1;
switch(K[1]){case"hsla":case"rgba":H=parseFloat(J[3]);
if(!H){return pv.Color.transparent
}break
}switch(K[1]){case"hsla":case"hsl":var E=parseFloat(J[0]),L=parseFloat(J[1])/100,B=parseFloat(J[2])/100;
return(new pv.Color.Hsl(E,L,B,H)).rgb();
case"rgba":case"rgb":function D(N){var M=parseFloat(N);
return(N[N.length-1]=="%")?Math.round(M*2.55):M
}var A=D(J[0]),F=D(J[1]),G=D(J[2]);
return pv.rgb(A,F,G,H)
}}var C=pv.Color.names[I];
if(C){return C
}if(I.charAt(0)=="#"){var A,F,G;
if(I.length==4){A=I.charAt(1);
A+=A;
F=I.charAt(2);
F+=F;
G=I.charAt(3);
G+=G
}else{if(I.length==7){A=I.substring(1,3);
F=I.substring(3,5);
G=I.substring(5,7)
}}return pv.rgb(parseInt(A,16),parseInt(F,16),parseInt(G,16),1)
}return new pv.Color(I,1)
};
pv.Color=function(A,B){this.color=A;
this.opacity=B
};
pv.Color.prototype.brighter=function(A){return this.rgb().brighter(A)
};
pv.Color.prototype.darker=function(A){return this.rgb().darker(A)
};
pv.rgb=function(D,C,A,B){return new pv.Color.Rgb(D,C,A,(arguments.length==4)?B:1)
};
pv.Color.Rgb=function(D,C,A,B){pv.Color.call(this,B?("rgb("+D+","+C+","+A+")"):"none",B);
this.r=D;
this.g=C;
this.b=A;
this.a=B
};
pv.Color.Rgb.prototype=pv.extend(pv.Color);
pv.Color.Rgb.prototype.red=function(A){return pv.rgb(A,this.g,this.b,this.a)
};
pv.Color.Rgb.prototype.green=function(A){return pv.rgb(this.r,A,this.b,this.a)
};
pv.Color.Rgb.prototype.blue=function(A){return pv.rgb(this.r,this.g,A,this.a)
};
pv.Color.Rgb.prototype.alpha=function(A){return pv.rgb(this.r,this.g,this.b,A)
};
pv.Color.Rgb.prototype.rgb=function(){return this
};
pv.Color.Rgb.prototype.brighter=function(B){B=Math.pow(0.7,arguments.length?B:1);
var E=this.r,D=this.g,A=this.b,C=30;
if(!E&&!D&&!A){return pv.rgb(C,C,C,this.a)
}if(E&&(E<C)){E=C
}if(D&&(D<C)){D=C
}if(A&&(A<C)){A=C
}return pv.rgb(Math.min(255,Math.floor(E/B)),Math.min(255,Math.floor(D/B)),Math.min(255,Math.floor(A/B)),this.a)
};
pv.Color.Rgb.prototype.darker=function(A){A=Math.pow(0.7,arguments.length?A:1);
return pv.rgb(Math.max(0,Math.floor(A*this.r)),Math.max(0,Math.floor(A*this.g)),Math.max(0,Math.floor(A*this.b)),this.a)
};
pv.hsl=function(D,C,B,A){return new pv.Color.Hsl(D,C,B,(arguments.length==4)?A:1)
};
pv.Color.Hsl=function(D,C,B,A){pv.Color.call(this,"hsl("+D+","+(C*100)+"%,"+(B*100)+"%)",A);
this.h=D;
this.s=C;
this.l=B;
this.a=A
};
pv.Color.Hsl.prototype=pv.extend(pv.Color);
pv.Color.Hsl.prototype.hue=function(A){return pv.hsl(A,this.s,this.l,this.a)
};
pv.Color.Hsl.prototype.saturation=function(A){return pv.hsl(this.h,A,this.l,this.a)
};
pv.Color.Hsl.prototype.lightness=function(A){return pv.hsl(this.h,this.s,A,this.a)
};
pv.Color.Hsl.prototype.alpha=function(A){return pv.hsl(this.h,this.s,this.l,A)
};
pv.Color.Hsl.prototype.rgb=function(){var F=this.h,E=this.s,A=this.l;
F=F%360;
if(F<0){F+=360
}E=Math.max(0,Math.min(E,1));
A=Math.max(0,Math.min(A,1));
var C=(A<=0.5)?(A*(1+E)):(A+E-A*E);
var D=2*A-C;
function B(H){if(H>360){H-=360
}else{if(H<0){H+=360
}}if(H<60){return D+(C-D)*H/60
}if(H<180){return C
}if(H<240){return D+(C-D)*(240-H)/60
}return D
}function G(H){return Math.round(B(H)*255)
}return pv.rgb(G(F+120),G(F),G(F-120),this.a)
};
pv.Color.names={aliceblue:"#f0f8ff",antiquewhite:"#faebd7",aqua:"#00ffff",aquamarine:"#7fffd4",azure:"#f0ffff",beige:"#f5f5dc",bisque:"#ffe4c4",black:"#000000",blanchedalmond:"#ffebcd",blue:"#0000ff",blueviolet:"#8a2be2",brown:"#a52a2a",burlywood:"#deb887",cadetblue:"#5f9ea0",chartreuse:"#7fff00",chocolate:"#d2691e",coral:"#ff7f50",cornflowerblue:"#6495ed",cornsilk:"#fff8dc",crimson:"#dc143c",cyan:"#00ffff",darkblue:"#00008b",darkcyan:"#008b8b",darkgoldenrod:"#b8860b",darkgray:"#a9a9a9",darkgreen:"#006400",darkgrey:"#a9a9a9",darkkhaki:"#bdb76b",darkmagenta:"#8b008b",darkolivegreen:"#556b2f",darkorange:"#ff8c00",darkorchid:"#9932cc",darkred:"#8b0000",darksalmon:"#e9967a",darkseagreen:"#8fbc8f",darkslateblue:"#483d8b",darkslategray:"#2f4f4f",darkslategrey:"#2f4f4f",darkturquoise:"#00ced1",darkviolet:"#9400d3",deeppink:"#ff1493",deepskyblue:"#00bfff",dimgray:"#696969",dimgrey:"#696969",dodgerblue:"#1e90ff",firebrick:"#b22222",floralwhite:"#fffaf0",forestgreen:"#228b22",fuchsia:"#ff00ff",gainsboro:"#dcdcdc",ghostwhite:"#f8f8ff",gold:"#ffd700",goldenrod:"#daa520",gray:"#808080",green:"#008000",greenyellow:"#adff2f",grey:"#808080",honeydew:"#f0fff0",hotpink:"#ff69b4",indianred:"#cd5c5c",indigo:"#4b0082",ivory:"#fffff0",khaki:"#f0e68c",lavender:"#e6e6fa",lavenderblush:"#fff0f5",lawngreen:"#7cfc00",lemonchiffon:"#fffacd",lightblue:"#add8e6",lightcoral:"#f08080",lightcyan:"#e0ffff",lightgoldenrodyellow:"#fafad2",lightgray:"#d3d3d3",lightgreen:"#90ee90",lightgrey:"#d3d3d3",lightpink:"#ffb6c1",lightsalmon:"#ffa07a",lightseagreen:"#20b2aa",lightskyblue:"#87cefa",lightslategray:"#778899",lightslategrey:"#778899",lightsteelblue:"#b0c4de",lightyellow:"#ffffe0",lime:"#00ff00",limegreen:"#32cd32",linen:"#faf0e6",magenta:"#ff00ff",maroon:"#800000",mediumaquamarine:"#66cdaa",mediumblue:"#0000cd",mediumorchid:"#ba55d3",mediumpurple:"#9370db",mediumseagreen:"#3cb371",mediumslateblue:"#7b68ee",mediumspringgreen:"#00fa9a",mediumturquoise:"#48d1cc",mediumvioletred:"#c71585",midnightblue:"#191970",mintcream:"#f5fffa",mistyrose:"#ffe4e1",moccasin:"#ffe4b5",navajowhite:"#ffdead",navy:"#000080",oldlace:"#fdf5e6",olive:"#808000",olivedrab:"#6b8e23",orange:"#ffa500",orangered:"#ff4500",orchid:"#da70d6",palegoldenrod:"#eee8aa",palegreen:"#98fb98",paleturquoise:"#afeeee",palevioletred:"#db7093",papayawhip:"#ffefd5",peachpuff:"#ffdab9",peru:"#cd853f",pink:"#ffc0cb",plum:"#dda0dd",powderblue:"#b0e0e6",purple:"#800080",red:"#ff0000",rosybrown:"#bc8f8f",royalblue:"#4169e1",saddlebrown:"#8b4513",salmon:"#fa8072",sandybrown:"#f4a460",seagreen:"#2e8b57",seashell:"#fff5ee",sienna:"#a0522d",silver:"#c0c0c0",skyblue:"#87ceeb",slateblue:"#6a5acd",slategray:"#708090",slategrey:"#708090",snow:"#fffafa",springgreen:"#00ff7f",steelblue:"#4682b4",tan:"#d2b48c",teal:"#008080",thistle:"#d8bfd8",tomato:"#ff6347",turquoise:"#40e0d0",violet:"#ee82ee",wheat:"#f5deb3",white:"#ffffff",whitesmoke:"#f5f5f5",yellow:"#ffff00",yellowgreen:"#9acd32",transparent:pv.Color.transparent=pv.rgb(0,0,0,0)};
(function(){var B=pv.Color.names;
for(var A in B){B[A]=pv.color(B[A])
}})();
pv.colors=function(){var A=pv.Scale.ordinal();
A.range.apply(A,arguments);
return A
};
pv.Colors={};
pv.Colors.category10=function(){var A=pv.colors("#1f77b4","#ff7f0e","#2ca02c","#d62728","#9467bd","#8c564b","#e377c2","#7f7f7f","#bcbd22","#17becf");
A.domain.apply(A,arguments);
return A
};
pv.Colors.category20=function(){var A=pv.colors("#1f77b4","#aec7e8","#ff7f0e","#ffbb78","#2ca02c","#98df8a","#d62728","#ff9896","#9467bd","#c5b0d5","#8c564b","#c49c94","#e377c2","#f7b6d2","#7f7f7f","#c7c7c7","#bcbd22","#dbdb8d","#17becf","#9edae5");
A.domain.apply(A,arguments);
return A
};
pv.Colors.category19=function(){var A=pv.colors("#9c9ede","#7375b5","#4a5584","#cedb9c","#b5cf6b","#8ca252","#637939","#e7cb94","#e7ba52","#bd9e39","#8c6d31","#e7969c","#d6616b","#ad494a","#843c39","#de9ed6","#ce6dbd","#a55194","#7b4173");
A.domain.apply(A,arguments);
return A
};
pv.ramp=function(C,A){var B=pv.Scale.linear();
B.range.apply(B,arguments);
return B
};
pv.Scene=pv.SvgScene={svg:"http://www.w3.org/2000/svg",xmlns:"http://www.w3.org/2000/xmlns",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",scale:1,events:["DOMMouseScroll","mousewheel","mousedown","mouseup","mouseover","mouseout","mousemove","click","dblclick"],implicit:{svg:{"shape-rendering":"auto","pointer-events":"painted",x:0,y:0,dy:0,"text-anchor":"start",transform:"translate(0,0)",fill:"none","fill-opacity":1,stroke:"none","stroke-opacity":1,"stroke-width":1.5,"stroke-linejoin":"miter"},css:{font:"10px sans-serif"}}};
pv.SvgScene.updateAll=function(A){if(A.length&&A[0].reverse&&(A.type!="line")&&(A.type!="area")){var D=pv.extend(A);
for(var C=0,B=A.length-1;
B>=0;
C++,B--){D[C]=A[B]
}A=D
}this.removeSiblings(this[A.type](A))
};
pv.SvgScene.create=function(A){return document.createElementNS(this.svg,A)
};
pv.SvgScene.expect=function(F,D,A,C){if(F){if(F.tagName=="a"){F=F.firstChild
}if(F.tagName!=D){var G=this.create(D);
F.parentNode.replaceChild(G,F);
F=G
}}else{F=this.create(D)
}for(var B in A){var E=A[B];
if(E==this.implicit.svg[B]){E=null
}if(E==null){F.removeAttribute(B)
}else{F.setAttribute(B,E)
}}for(var B in C){var E=C[B];
if(E==this.implicit.css[B]){E=null
}if(E==null){F.style.removeProperty(B)
}else{F.style[B]=E
}}return F
};
pv.SvgScene.append=function(C,A,B){C.$scene={scenes:A,index:B};
C=this.title(C,A[B]);
if(!C.parentNode){A.$g.appendChild(C)
}return C.nextSibling
};
pv.SvgScene.title=function(C,B){var A=C.parentNode;
if(A&&(A.tagName!="a")){A=null
}if(B.title){if(!A){A=this.create("a");
if(C.parentNode){C.parentNode.replaceChild(A,C)
}A.appendChild(C)
}A.setAttributeNS(this.xlink,"title",B.title);
return A
}if(A){A.parentNode.replaceChild(C,A)
}return C
};
pv.SvgScene.dispatch=pv.listener(function(C){var A=C.target.$scene;
if(A){var B=C.type;
switch(B){case"DOMMouseScroll":B="mousewheel";
C.wheel=-480*C.detail;
break;
case"mousewheel":C.wheel=(window.opera?12:1)*C.wheelDelta;
break
}if(pv.Mark.dispatch(B,A.scenes,A.index)){C.preventDefault()
}}});
pv.SvgScene.removeSiblings=function(A){while(A){var B=A.nextSibling;
A.parentNode.removeChild(A);
A=B
}};
pv.SvgScene.undefined=function(){};
pv.SvgScene.pathBasis=(function(){var C=[[1/6,2/3,1/6,0],[0,2/3,1/3,0],[0,1/3,2/3,0],[0,1/6,2/3,1/6]];
function A(D,H,G,F,E){return{x:D[0]*H.left+D[1]*G.left+D[2]*F.left+D[3]*E.left,y:D[0]*H.top+D[1]*G.top+D[2]*F.top+D[3]*E.top}
}var B=function(J,I,H,G){var F=A(C[1],J,I,H,G),E=A(C[2],J,I,H,G),D=A(C[3],J,I,H,G);
return"C"+F.x+","+F.y+","+E.x+","+E.y+","+D.x+","+D.y
};
B.segment=function(K,J,I,H){var G=A(C[0],K,J,I,H),F=A(C[1],K,J,I,H),E=A(C[2],K,J,I,H),D=A(C[3],K,J,I,H);
return"M"+G.x+","+G.y+"C"+F.x+","+F.y+","+E.x+","+E.y+","+D.x+","+D.y
};
return B
})();
pv.SvgScene.curveBasis=function(B){if(B.length<=2){return""
}var E="",G=B[0],F=G,D=G,C=B[1];
E+=this.pathBasis(G,F,D,C);
for(var A=2;
A<B.length;
A++){G=F;
F=D;
D=C;
C=B[A];
E+=this.pathBasis(G,F,D,C)
}E+=this.pathBasis(F,D,C,C);
E+=this.pathBasis(D,C,C,C);
return E
};
pv.SvgScene.curveBasisSegments=function(B){if(B.length<=2){return""
}var F=[],H=B[0],E=H,D=H,C=B[1],G=this.pathBasis.segment(H,E,D,C);
H=E;
E=D;
D=C;
C=B[2];
F.push(G+this.pathBasis(H,E,D,C));
for(var A=3;
A<B.length;
A++){H=E;
E=D;
D=C;
C=B[A];
F.push(this.pathBasis.segment(H,E,D,C))
}F.push(this.pathBasis.segment(E,D,C,C)+this.pathBasis(D,C,C,C));
return F
};
pv.SvgScene.curveHermite=function(G,F){if(F.length<1||(G.length!=F.length&&G.length!=F.length+2)){return""
}var H=G.length!=F.length,K="",I=G[0],A=G[1],E=F[0],J=E,C=1;
if(H){K+="Q"+(A.left-E.x*2/3)+","+(A.top-E.y*2/3)+","+A.left+","+A.top;
I=G[1];
C=2
}if(F.length>1){J=F[1];
A=G[C];
C++;
K+="C"+(I.left+E.x)+","+(I.top+E.y)+","+(A.left-J.x)+","+(A.top-J.y)+","+A.left+","+A.top;
for(var B=2;
B<F.length;
B++,C++){A=G[C];
J=F[B];
K+="S"+(A.left-J.x)+","+(A.top-J.y)+","+A.left+","+A.top
}}if(H){var D=G[C];
K+="Q"+(A.left+J.x*2/3)+","+(A.top+J.y*2/3)+","+D.left+","+D.top
}return K
};
pv.SvgScene.curveHermiteSegments=function(G,F){if(F.length<1||(G.length!=F.length&&G.length!=F.length+2)){return[]
}var H=G.length!=F.length,K=[],I=G[0],A=I,E=F[0],J=E,C=1;
if(H){A=G[1];
K.push("M"+I.left+","+I.top+"Q"+(A.left-J.x*2/3)+","+(A.top-J.y*2/3)+","+A.left+","+A.top);
C=2
}for(var B=1;
B<F.length;
B++,C++){I=A;
E=J;
A=G[C];
J=F[B];
K.push("M"+I.left+","+I.top+"C"+(I.left+E.x)+","+(I.top+E.y)+","+(A.left-J.x)+","+(A.top-J.y)+","+A.left+","+A.top)
}if(H){var D=G[C];
K.push("M"+A.left+","+A.top+"Q"+(A.left+J.x*2/3)+","+(A.top+J.y*2/3)+","+D.left+","+D.top)
}return K
};
pv.SvgScene.cardinalTangents=function(E,D){var B=[],A=(1-D)/2,H=E[0],G=E[1],F=E[2];
for(var C=3;
C<E.length;
C++){B.push({x:A*(F.left-H.left),y:A*(F.top-H.top)});
H=G;
G=F;
F=E[C]
}B.push({x:A*(F.left-H.left),y:A*(F.top-H.top)});
return B
};
pv.SvgScene.curveCardinal=function(B,A){if(B.length<=2){return""
}return this.curveHermite(B,this.cardinalTangents(B,A))
};
pv.SvgScene.curveCardinalSegments=function(B,A){if(B.length<=2){return""
}return this.curveHermiteSegments(B,this.cardinalTangents(B,A))
};
pv.SvgScene.monotoneTangents=function(I){var H=[],G=[],A=[],L=[],C=0;
for(C=0;
C<I.length-1;
C++){G[C]=(I[C+1].top-I[C].top)/(I[C+1].left-I[C].left)
}A[0]=G[0];
L[0]=I[1].left-I[0].left;
for(C=1;
C<I.length-1;
C++){A[C]=(G[C-1]+G[C])/2;
L[C]=(I[C+1].left-I[C-1].left)/2
}A[C]=G[C-1];
L[C]=(I[C].left-I[C-1].left);
for(C=0;
C<I.length-1;
C++){if(G[C]==0){A[C]=0;
A[C+1]=0
}}for(C=0;
C<I.length-1;
C++){if((Math.abs(A[C])<0.00001)||(Math.abs(A[C+1])<0.00001)){continue
}var F=A[C]/G[C],B=A[C+1]/G[C],K=F*F+B*B;
if(K>9){var J=3/Math.sqrt(K);
A[C]=J*F*G[C];
A[C+1]=J*B*G[C]
}}var E;
for(var D=0;
D<I.length;
D++){E=1+A[D]*A[D];
H.push({x:L[D]/3/E,y:A[D]*L[D]/3/E})
}return H
};
pv.SvgScene.curveMonotone=function(A){if(A.length<=2){return""
}return this.curveHermite(A,this.monotoneTangents(A))
};
pv.SvgScene.curveMonotoneSegments=function(A){if(A.length<=2){return""
}return this.curveHermiteSegments(A,this.monotoneTangents(A))
};
pv.SvgScene.area=function(A){var F=A.$g.firstChild;
if(!A.length){return F
}var L=A[0];
if(L.segmented){return this.areaSegment(A)
}if(!L.visible){return F
}var I=L.fillStyle,J=L.strokeStyle;
if(!I.opacity&&!J.opacity){return F
}function K(S,P){var W=[],V=[];
for(var O=P;
S<=O;
S++,P--){var T=A[S],Q=A[P],U=T.left+","+T.top,R=(Q.left+Q.width)+","+(Q.top+Q.height);
if(S<O){var N=A[S+1],M=A[P-1];
switch(L.interpolate){case"step-before":U+="V"+N.top;
R+="H"+(M.left+M.width);
break;
case"step-after":U+="H"+N.left;
R+="V"+(M.top+M.height);
break
}}W.push(U);
V.push(R)
}return W.concat(V).join("L")
}function H(R,Q){var M=[],T=[],S,P;
for(var O=Q;
R<=O;
R++,Q--){var N=A[Q];
M.push(A[R]);
T.push({left:N.left+N.width,top:N.top+N.height})
}if(L.interpolate=="basis"){S=pv.SvgScene.curveBasis(M);
P=pv.SvgScene.curveBasis(T)
}else{if(L.interpolate=="cardinal"){S=pv.SvgScene.curveCardinal(M,L.tension);
P=pv.SvgScene.curveCardinal(T,L.tension)
}else{S=pv.SvgScene.curveMonotone(M);
P=pv.SvgScene.curveMonotone(T)
}}return M[0].left+","+M[0].top+S+"L"+T[0].left+","+T[0].top+P
}var G=[],E,C;
for(var D=0;
D<A.length;
D++){E=A[D];
if(!E.width&&!E.height){continue
}for(var B=D+1;
B<A.length;
B++){C=A[B];
if(!C.width&&!C.height){break
}}if(D&&(L.interpolate!="step-after")){D--
}if((B<A.length)&&(L.interpolate!="step-before")){B++
}G.push(((B-D>2&&(L.interpolate=="basis"||L.interpolate=="cardinal"||L.interpolate=="monotone"))?H:K)(D,B-1));
D=B-1
}if(!G.length){return F
}F=this.expect(F,"path",{"shape-rendering":L.antialias?null:"crispEdges","pointer-events":L.events,cursor:L.cursor,d:"M"+G.join("ZM")+"Z",fill:I.color,"fill-opacity":I.opacity||null,stroke:J.color,"stroke-opacity":J.opacity||null,"stroke-width":J.opacity?L.lineWidth/this.scale:null});
return this.append(F,A,0)
};
pv.SvgScene.areaSegment=function(A){var O=A.$g.firstChild,I=A[0],M,E;
if(I.interpolate=="basis"||I.interpolate=="cardinal"||I.interpolate=="monotone"){var D=[],H=[];
for(var N=0,J=A.length;
N<J;
N++){var Q=A[J-N-1];
D.push(A[N]);
H.push({left:Q.left+Q.width,top:Q.top+Q.height})
}if(I.interpolate=="basis"){M=this.curveBasisSegments(D);
E=this.curveBasisSegments(H)
}else{if(I.interpolate=="cardinal"){M=this.curveCardinalSegments(D,I.tension);
E=this.curveCardinalSegments(H,I.tension)
}else{M=this.curveMonotoneSegments(D);
E=this.curveMonotoneSegments(H)
}}}for(var N=0,J=A.length-1;
N<J;
N++){var C=A[N],B=A[N+1];
if(!C.visible||!B.visible){continue
}var L=C.fillStyle,G=C.strokeStyle;
if(!L.opacity&&!G.opacity){continue
}var P;
if(M){var F=M[N],K="L"+E[J-N-1].substr(1);
P=F+K+"Z"
}else{var R=C,Q=B;
switch(C.interpolate){case"step-before":R=B;
break;
case"step-after":Q=C;
break
}P="M"+C.left+","+R.top+"L"+B.left+","+Q.top+"L"+(B.left+B.width)+","+(Q.top+Q.height)+"L"+(C.left+C.width)+","+(R.top+R.height)+"Z"
}O=this.expect(O,"path",{"shape-rendering":C.antialias?null:"crispEdges","pointer-events":C.events,cursor:C.cursor,d:P,fill:L.color,"fill-opacity":L.opacity||null,stroke:G.color,"stroke-opacity":G.opacity||null,"stroke-width":G.opacity?C.lineWidth/this.scale:null});
O=this.append(O,A,N)
}return O
};
pv.SvgScene.bar=function(A){var F=A.$g.firstChild;
for(var B=0;
B<A.length;
B++){var C=A[B];
if(!C.visible){continue
}var E=C.fillStyle,D=C.strokeStyle;
if(!E.opacity&&!D.opacity){continue
}F=this.expect(F,"rect",{"shape-rendering":C.antialias?null:"crispEdges","pointer-events":C.events,cursor:C.cursor,x:C.left,y:C.top,width:Math.max(1e-10,C.width),height:Math.max(1e-10,C.height),fill:E.color,"fill-opacity":E.opacity||null,stroke:D.color,"stroke-opacity":D.opacity||null,"stroke-width":D.opacity?C.lineWidth/this.scale:null});
F=this.append(F,A,B)
}return F
};
pv.SvgScene.dot=function(A){var F=A.$g.firstChild;
for(var B=0;
B<A.length;
B++){var K=A[B];
if(!K.visible){continue
}var H=K.fillStyle,I=K.strokeStyle;
if(!H.opacity&&!I.opacity){continue
}var E=K.radius,J=null;
switch(K.shape){case"cross":J="M"+-E+","+-E+"L"+E+","+E+"M"+E+","+-E+"L"+-E+","+E;
break;
case"triangle":var D=E,G=E*1.1547;
J="M0,"+D+"L"+G+","+-D+" "+-G+","+-D+"Z";
break;
case"diamond":E*=Math.SQRT2;
J="M0,"+-E+"L"+E+",0 0,"+E+" "+-E+",0Z";
break;
case"square":J="M"+-E+","+-E+"L"+E+","+-E+" "+E+","+E+" "+-E+","+E+"Z";
break;
case"tick":J="M0,0L0,"+-K.size;
break;
case"bar":J="M0,"+(K.size/2)+"L0,"+-(K.size/2);
break
}var C={"shape-rendering":K.antialias?null:"crispEdges","pointer-events":K.events,cursor:K.cursor,fill:H.color,"fill-opacity":H.opacity||null,stroke:I.color,"stroke-opacity":I.opacity||null,"stroke-width":I.opacity?K.lineWidth/this.scale:null};
if(J){C.transform="translate("+K.left+","+K.top+")";
if(K.angle){C.transform+=" rotate("+180*K.angle/Math.PI+")"
}C.d=J;
F=this.expect(F,"path",C)
}else{C.cx=K.left;
C.cy=K.top;
C.r=E;
F=this.expect(F,"circle",C)
}F=this.append(F,A,B)
}return F
};
pv.SvgScene.image=function(A){var D=A.$g.firstChild;
for(var B=0;
B<A.length;
B++){var C=A[B];
if(!C.visible){continue
}D=this.fill(D,A,B);
if(C.image){D=this.expect(D,"foreignObject",{cursor:C.cursor,x:C.left,y:C.top,width:C.width,height:C.height});
var E=D.firstChild||D.appendChild(document.createElementNS(this.xhtml,"canvas"));
E.$scene={scenes:A,index:B};
E.style.width=C.width;
E.style.height=C.height;
E.width=C.imageWidth;
E.height=C.imageHeight;
E.getContext("2d").putImageData(C.image,0,0)
}else{D=this.expect(D,"image",{preserveAspectRatio:"none",cursor:C.cursor,x:C.left,y:C.top,width:C.width,height:C.height});
D.setAttributeNS(this.xlink,"href",C.url)
}D=this.append(D,A,B);
D=this.stroke(D,A,B)
}return D
};
pv.SvgScene.label=function(A){var D=A.$g.firstChild;
for(var C=0;
C<A.length;
C++){var I=A[C];
if(!I.visible){continue
}var G=I.textStyle;
if(!G.opacity||!I.text){continue
}var F=0,E=0,H=0,B="start";
switch(I.textBaseline){case"middle":H=".35em";
break;
case"top":H=".71em";
E=I.textMargin;
break;
case"bottom":E="-"+I.textMargin;
break
}switch(I.textAlign){case"right":B="end";
F="-"+I.textMargin;
break;
case"center":B="middle";
break;
case"left":F=I.textMargin;
break
}D=this.expect(D,"text",{"pointer-events":I.events,cursor:I.cursor,x:F,y:E,dy:H,transform:"translate("+I.left+","+I.top+")"+(I.textAngle?" rotate("+180*I.textAngle/Math.PI+")":"")+(this.scale!=1?" scale("+1/this.scale+")":""),fill:G.color,"fill-opacity":G.opacity||null,"text-anchor":B},{font:I.font,"text-shadow":I.textShadow,"text-decoration":I.textDecoration});
if(D.firstChild){D.firstChild.nodeValue=I.text
}else{D.appendChild(document.createTextNode(I.text))
}D=this.append(D,A,C)
}return D
};
pv.SvgScene.line=function(A){var F=A.$g.firstChild;
if(A.length<2){return F
}var C=A[0];
if(C.segmented){return this.lineSegment(A)
}if(!C.visible){return F
}var E=C.fillStyle,D=C.strokeStyle;
if(!E.opacity&&!D.opacity){return F
}var G="M"+C.left+","+C.top;
if(A.length>2&&(C.interpolate=="basis"||C.interpolate=="cardinal"||C.interpolate=="monotone")){switch(C.interpolate){case"basis":G+=this.curveBasis(A);
break;
case"cardinal":G+=this.curveCardinal(A,C.tension);
break;
case"monotone":G+=this.curveMonotone(A);
break
}}else{for(var B=1;
B<A.length;
B++){G+=this.pathSegment(A[B-1],A[B])
}}F=this.expect(F,"path",{"shape-rendering":C.antialias?null:"crispEdges","pointer-events":C.events,cursor:C.cursor,d:G,fill:E.color,"fill-opacity":E.opacity||null,stroke:D.color,"stroke-opacity":D.opacity||null,"stroke-width":D.opacity?C.lineWidth/this.scale:null,"stroke-linejoin":C.lineJoin});
return this.append(F,A,0)
};
pv.SvgScene.lineSegment=function(A){var D=A.$g.firstChild;
var K=A[0];
var J;
switch(K.interpolate){case"basis":J=this.curveBasisSegments(A);
break;
case"cardinal":J=this.curveCardinalSegments(A,K.tension);
break;
case"monotone":J=this.curveMonotoneSegments(A);
break
}for(var C=0,B=A.length-1;
C<B;
C++){var G=A[C],F=A[C+1];
if(!G.visible||!F.visible){continue
}var I=G.strokeStyle,H=pv.Color.transparent;
if(!I.opacity){continue
}var E;
if((G.interpolate=="linear")&&(G.lineJoin=="miter")){H=I;
I=pv.Color.transparent;
E=this.pathJoin(A[C-1],G,F,A[C+2])
}else{if(J){E=J[C]
}else{E="M"+G.left+","+G.top+this.pathSegment(G,F)
}}D=this.expect(D,"path",{"shape-rendering":G.antialias?null:"crispEdges","pointer-events":G.events,cursor:G.cursor,d:E,fill:H.color,"fill-opacity":H.opacity||null,stroke:I.color,"stroke-opacity":I.opacity||null,"stroke-width":I.opacity?G.lineWidth/this.scale:null,"stroke-linejoin":G.lineJoin});
D=this.append(D,A,C)
}return D
};
pv.SvgScene.pathSegment=function(E,D){var B=1;
switch(E.interpolate){case"polar-reverse":B=0;
case"polar":var C=D.left-E.left,A=D.top-E.top,G=1-E.eccentricity,F=Math.sqrt(C*C+A*A)/(2*G);
if((G<=0)||(G>1)){break
}return"A"+F+","+F+" 0 0,"+B+" "+D.left+","+D.top;
case"step-before":return"V"+D.top+"H"+D.left;
case"step-after":return"H"+D.left+"V"+D.top
}return"L"+D.left+","+D.top
};
pv.SvgScene.lineIntersect=function(D,C,B,A){return D.plus(C.times(B.minus(D).dot(A.perp())/C.dot(A.perp())))
};
pv.SvgScene.pathJoin=function(O,L,J,G){var N=pv.vector(L.left,L.top),K=pv.vector(J.left,J.top),A=K.minus(N),M=A.perp().norm(),I=M.times(L.lineWidth/(2*this.scale)),F=N.plus(I),D=K.plus(I),C=K.minus(I),B=N.minus(I);
if(O&&O.visible){var H=N.minus(O.left,O.top).perp().norm().plus(M);
B=this.lineIntersect(N,H,B,A);
F=this.lineIntersect(N,H,F,A)
}if(G&&G.visible){var E=pv.vector(G.left,G.top).minus(K).perp().norm().plus(M);
C=this.lineIntersect(K,E,C,A);
D=this.lineIntersect(K,E,D,A)
}return"M"+F.x+","+F.y+"L"+D.x+","+D.y+" "+C.x+","+C.y+" "+B.x+","+B.y
};
pv.SvgScene.panel=function(B){var G=B.$g,H=G&&G.firstChild;
for(var F=0;
F<B.length;
F++){var M=B[F];
if(!M.visible){continue
}if(!B.parent){M.canvas.style.display="inline-block";
if(G&&(G.parentNode!=M.canvas)){G=M.canvas.firstChild;
H=G&&G.firstChild
}if(!G){G=M.canvas.appendChild(this.create("svg"));
G.setAttribute("font-size","10px");
G.setAttribute("font-family","sans-serif");
G.setAttribute("fill","none");
G.setAttribute("stroke","none");
G.setAttribute("stroke-width",1.5);
for(var E=0;
E<this.events.length;
E++){G.addEventListener(this.events[E],this.dispatch,false)
}H=G.firstChild
}B.$g=G;
G.setAttribute("width",M.width+M.left+M.right);
G.setAttribute("height",M.height+M.top+M.bottom)
}if(M.overflow=="hidden"){var C=pv.id().toString(36),I=this.expect(H,"g",{"clip-path":"url(#"+C+")"});
if(!I.parentNode){G.appendChild(I)
}B.$g=G=I;
H=I.firstChild;
H=this.expect(H,"clipPath",{id:C});
var A=H.firstChild||H.appendChild(this.create("rect"));
A.setAttribute("x",M.left);
A.setAttribute("y",M.top);
A.setAttribute("width",M.width);
A.setAttribute("height",M.height);
if(!H.parentNode){G.appendChild(H)
}H=H.nextSibling
}H=this.fill(H,B,F);
var D=this.scale,L=M.transform,K=M.left+L.x,J=M.top+L.y;
this.scale*=L.k;
for(var E=0;
E<M.children.length;
E++){M.children[E].$g=H=this.expect(H,"g",{transform:"translate("+K+","+J+")"+(L.k!=1?" scale("+L.k+")":"")});
this.updateAll(M.children[E]);
if(!H.parentNode){G.appendChild(H)
}H=H.nextSibling
}this.scale=D;
H=this.stroke(H,B,F);
if(M.overflow=="hidden"){B.$g=G=I.parentNode;
H=I.nextSibling
}}return H
};
pv.SvgScene.fill=function(E,A,B){var C=A[B],D=C.fillStyle;
if(D.opacity||C.events=="all"){E=this.expect(E,"rect",{"shape-rendering":C.antialias?null:"crispEdges","pointer-events":C.events,cursor:C.cursor,x:C.left,y:C.top,width:C.width,height:C.height,fill:D.color,"fill-opacity":D.opacity,stroke:null});
E=this.append(E,A,B)
}return E
};
pv.SvgScene.stroke=function(E,A,B){var C=A[B],D=C.strokeStyle;
if(D.opacity||C.events=="all"){E=this.expect(E,"rect",{"shape-rendering":C.antialias?null:"crispEdges","pointer-events":C.events=="all"?"stroke":C.events,cursor:C.cursor,x:C.left,y:C.top,width:Math.max(1e-10,C.width),height:Math.max(1e-10,C.height),fill:null,stroke:D.color,"stroke-opacity":D.opacity,"stroke-width":C.lineWidth/this.scale});
E=this.append(E,A,B)
}return E
};
pv.SvgScene.rule=function(A){var E=A.$g.firstChild;
for(var B=0;
B<A.length;
B++){var C=A[B];
if(!C.visible){continue
}var D=C.strokeStyle;
if(!D.opacity){continue
}E=this.expect(E,"line",{"shape-rendering":C.antialias?null:"crispEdges","pointer-events":C.events,cursor:C.cursor,x1:C.left,y1:C.top,x2:C.left+C.width,y2:C.top+C.height,stroke:D.color,"stroke-opacity":D.opacity,"stroke-width":C.lineWidth/this.scale});
E=this.append(E,A,B)
}return E
};
pv.SvgScene.wedge=function(A){var H=A.$g.firstChild;
for(var G=0;
G<A.length;
G++){var P=A[G];
if(!P.visible){continue
}var N=P.fillStyle,O=P.strokeStyle;
if(!N.opacity&&!O.opacity){continue
}var D=P.innerRadius,C=P.outerRadius,K=Math.abs(P.angle),B;
if(K>=2*Math.PI){if(D){B="M0,"+C+"A"+C+","+C+" 0 1,1 0,"+(-C)+"A"+C+","+C+" 0 1,1 0,"+C+"M0,"+D+"A"+D+","+D+" 0 1,1 0,"+(-D)+"A"+D+","+D+" 0 1,1 0,"+D+"Z"
}else{B="M0,"+C+"A"+C+","+C+" 0 1,1 0,"+(-C)+"A"+C+","+C+" 0 1,1 0,"+C+"Z"
}}else{var J=Math.min(P.startAngle,P.endAngle),I=Math.max(P.startAngle,P.endAngle),F=Math.cos(J),E=Math.cos(I),M=Math.sin(J),L=Math.sin(I);
if(D){B="M"+C*F+","+C*M+"A"+C+","+C+" 0 "+((K<Math.PI)?"0":"1")+",1 "+C*E+","+C*L+"L"+D*E+","+D*L+"A"+D+","+D+" 0 "+((K<Math.PI)?"0":"1")+",0 "+D*F+","+D*M+"Z"
}else{B="M"+C*F+","+C*M+"A"+C+","+C+" 0 "+((K<Math.PI)?"0":"1")+",1 "+C*E+","+C*L+"L0,0Z"
}}H=this.expect(H,"path",{"shape-rendering":P.antialias?null:"crispEdges","pointer-events":P.events,cursor:P.cursor,transform:"translate("+P.left+","+P.top+")",d:B,fill:N.color,"fill-rule":"evenodd","fill-opacity":N.opacity||null,stroke:O.color,"stroke-opacity":O.opacity||null,"stroke-width":O.opacity?P.lineWidth/this.scale:null});
H=this.append(H,A,G)
}return H
};
pv.Mark=function(){this.$properties=[];
this.$handlers={}
};
pv.Mark.prototype.properties={};
pv.Mark.cast={};
pv.Mark.prototype.property=function(B,A){if(!this.hasOwnProperty("properties")){this.properties=pv.extend(this.properties)
}this.properties[B]=true;
pv.Mark.prototype.propertyMethod(B,false,pv.Mark.cast[B]=A);
return this
};
pv.Mark.prototype.propertyMethod=function(B,C,A){if(!A){A=pv.Mark.cast[B]
}this[B]=function(E){if(C&&this.scene){var D=this.scene.defs;
if(arguments.length){D[B]={id:(E==null)?0:pv.id(),value:((E!=null)&&A)?A(E):E};
return this
}return D[B]?D[B].value:null
}if(arguments.length){var F=!C<<1|(typeof E=="function");
this.propertyValue(B,(F&1&&A)?function(){var G=E.apply(this,arguments);
return(G!=null)?A(G):null
}:(((E!=null)&&A)?A(E):E)).type=F;
return this
}return this.instance()[B]
}
};
pv.Mark.prototype.propertyValue=function(B,A){var D=this.$properties,E={name:B,id:pv.id(),value:A};
for(var C=0;
C<D.length;
C++){if(D[C].name==B){D.splice(C,1);
break
}}D.push(E);
return E
};
pv.Mark.prototype.property("data").property("visible",Boolean).property("left",Number).property("right",Number).property("top",Number).property("bottom",Number).property("cursor",String).property("title",String).property("reverse",Boolean).property("antialias",Boolean).property("events",String);
pv.Mark.prototype.childIndex=-1;
pv.Mark.prototype.index=-1;
pv.Mark.prototype.scale=1;
pv.Mark.prototype.defaults=new pv.Mark().data(function(A){return[A]
}).visible(true).antialias(true).events("painted");
pv.Mark.prototype.extend=function(A){this.proto=A;
this.target=A.target;
return this
};
pv.Mark.prototype.add=function(A){return this.parent.add(A).extend(this)
};
pv.Mark.prototype.def=function(B,A){this.propertyMethod(B,true);
return this[B](arguments.length>1?A:null)
};
pv.Mark.prototype.anchor=function(A){if(!A){A="center"
}return new pv.Anchor(this).name(A).data(function(){return this.scene.target.map(function(B){return B.data
})
}).visible(function(){return this.scene.target[this.index].visible
}).left(function(){var C=this.scene.target[this.index],B=C.width||0;
switch(this.name()){case"bottom":case"top":case"center":return C.left+B/2;
case"left":return null
}return C.left+B
}).top(function(){var C=this.scene.target[this.index],B=C.height||0;
switch(this.name()){case"left":case"right":case"center":return C.top+B/2;
case"top":return null
}return C.top+B
}).right(function(){var B=this.scene.target[this.index];
return this.name()=="left"?B.right+(B.width||0):null
}).bottom(function(){var B=this.scene.target[this.index];
return this.name()=="top"?B.bottom+(B.height||0):null
}).textAlign(function(){switch(this.name()){case"bottom":case"top":case"center":return"center";
case"right":return"right"
}return"left"
}).textBaseline(function(){switch(this.name()){case"right":case"left":case"center":return"middle";
case"top":return"top"
}return"bottom"
})
};
pv.Mark.prototype.anchorTarget=function(){return this.target
};
pv.Mark.prototype.margin=function(A){return this.left(A).right(A).top(A).bottom(A)
};
pv.Mark.prototype.instance=function(C){var B=this.scene||this.parent.instance(-1).children[this.childIndex],A=!arguments.length||this.hasOwnProperty("index")?this.index:C;
return B[A<0?B.length-1:A]
};
pv.Mark.prototype.instances=function(D){var F=this,A=[],E;
while(!(E=F.scene)){D=D.parent;
A.push({index:D.index,childIndex:F.childIndex});
F=F.parent
}while(A.length){var B=A.pop();
E=E[B.index].children[B.childIndex]
}if(this.hasOwnProperty("index")){var C=pv.extend(E[this.index]);
C.right=C.top=C.left=C.bottom=0;
return[C]
}return E
};
pv.Mark.prototype.first=function(){return this.scene[0]
};
pv.Mark.prototype.last=function(){return this.scene[this.scene.length-1]
};
pv.Mark.prototype.sibling=function(){return(this.index==0)?null:this.scene[this.index-1]
};
pv.Mark.prototype.cousin=function(){var B=this.parent,A=B&&B.sibling();
return(A&&A.children)?A.children[this.childIndex][this.index]:null
};
pv.Mark.prototype.render=function(){var D=this.parent,A=pv.Mark.stack;
if(D&&!this.root.scene){this.root.render();
return 
}var B=[];
for(var F=this;
F.parent;
F=F.parent){B.unshift(F.childIndex)
}function C(K,I,H){K.scale=H;
if(I<B.length){A.unshift(null);
if(K.hasOwnProperty("index")){E(K,I,H)
}else{for(var G=0,J=K.scene.length;
G<J;
G++){K.index=G;
E(K,I,H)
}delete K.index
}A.shift()
}else{K.build();
pv.Scene.scale=H;
pv.Scene.updateAll(K.scene)
}delete K.scale
}function E(M,J,I){var H=M.scene[M.index],G;
if(H.visible){var L=B[J],K=M.children[L];
for(G=0;
G<L;
G++){M.children[G].scene=H.children[G]
}A[0]=H.data;
if(K.scene){C(K,J+1,I*H.transform.k)
}else{K.scene=H.children[L];
C(K,J+1,I*H.transform.k);
delete K.scene
}for(G=0;
G<L;
G++){delete M.children[G].scene
}}}this.bind();
while(D&&!D.hasOwnProperty("index")){D=D.parent
}this.context(D?D.scene:undefined,D?D.index:-1,function(){C(this.root,0,1)
})
};
pv.Mark.stack=[];
pv.Mark.prototype.bind=function(){var A={},H=[[],[],[],[]],G,C;
function I(M){do{var K=M.$properties;
for(var J=K.length-1;
J>=0;
J--){var L=K[J];
if(!(L.name in A)){A[L.name]=L;
switch(L.name){case"data":G=L;
break;
case"visible":C=L;
break;
default:H[L.type].push(L);
break
}}}}while(M=M.proto)
}I(this);
I(this.defaults);
H[1].reverse();
H[3].reverse();
var D=this;
do{for(var B in D.properties){if(!(B in A)){H[2].push(A[B]={name:B,type:2,value:null})
}}}while(D=D.proto);
var E=H[0].concat(H[1]);
for(var F=0;
F<E.length;
F++){this.propertyMethod(E[F].name,true)
}this.binds={properties:A,data:G,defs:E,required:[C],optional:pv.blend(H)}
};
pv.Mark.prototype.build=function(){var G=this.scene,B=pv.Mark.stack;
if(!G){G=this.scene=[];
G.mark=this;
G.type=this.type;
G.childIndex=this.childIndex;
if(this.parent){G.parent=this.parent.scene;
G.parentIndex=this.parent.index
}}if(this.target){G.target=this.target.instances(G)
}if(this.binds.defs.length){var A=G.defs;
if(!A){G.defs=A={}
}for(var C=0;
C<this.binds.defs.length;
C++){var F=this.binds.defs[C],H=A[F.name];
if(!H||(F.id>H.id)){A[F.name]={id:0,value:(F.type&1)?F.value.apply(this,B):F.value}
}}}var E=this.binds.data;
E=E.type&1?E.value.apply(this,B):E.value;
B.unshift(null);
G.length=E.length;
for(var C=0;
C<E.length;
C++){pv.Mark.prototype.index=this.index=C;
var D=G[C];
if(!D){G[C]=D={}
}D.data=B[0]=E[C];
this.buildInstance(D)
}pv.Mark.prototype.index=-1;
delete this.index;
B.shift();
return this
};
pv.Mark.prototype.buildProperties=function(D,C){for(var B=0,F=C.length;
B<F;
B++){var E=C[B],A=E.value;
switch(E.type){case 0:case 1:A=this.scene.defs[E.name].value;
break;
case 3:A=A.apply(this,pv.Mark.stack);
break
}D[E.name]=A
}};
pv.Mark.prototype.buildInstance=function(A){this.buildProperties(A,this.binds.required);
if(A.visible){this.buildProperties(A,this.binds.optional);
this.buildImplied(A)
}};
pv.Mark.prototype.buildImplied=function(J){var D=J.left;
var A=J.right;
var I=J.top;
var F=J.bottom;
var C=this.properties;
var G=C.width?J.width:0;
var E=C.height?J.height:0;
var B=this.parent?this.parent.width():(G+D+A);
if(G==null){G=B-(A=A||0)-(D=D||0)
}else{if(A==null){if(D==null){D=A=(B-G)/2
}else{A=B-G-(D=D||0)
}}else{if(D==null){D=B-G-A
}}}var H=this.parent?this.parent.height():(E+I+F);
if(E==null){E=H-(I=I||0)-(F=F||0)
}else{if(F==null){if(I==null){F=I=(H-E)/2
}else{F=H-E-(I=I||0)
}}else{if(I==null){I=H-E-F
}}}J.left=D;
J.right=A;
J.top=I;
J.bottom=F;
if(C.width){J.width=G
}if(C.height){J.height=E
}if(C.textStyle&&!J.textStyle){J.textStyle=pv.Color.transparent
}if(C.fillStyle&&!J.fillStyle){J.fillStyle=pv.Color.transparent
}if(C.strokeStyle&&!J.strokeStyle){J.strokeStyle=pv.Color.transparent
}};
pv.Mark.prototype.mouse=function(){var A=pv.event.pageX||0,F=pv.event.pageY||0,E=this.root.canvas();
do{A-=E.offsetLeft;
F-=E.offsetTop
}while(E=E.offsetParent);
var C=pv.Transform.identity,D=this.properties.transform?this:this.parent,B=[];
do{B.push(D)
}while(D=D.parent);
while(D=B.pop()){C=C.translate(D.left(),D.top()).times(D.transform())
}C=C.invert();
return pv.vector(A*C.k+C.x,F*C.k+C.y)
};
pv.Mark.prototype.event=function(B,A){this.$handlers[B]=pv.functor(A);
return this
};
pv.Mark.prototype.context=function(B,F,E){var C=pv.Mark.prototype,H=pv.Mark.stack,D=pv.Mark.scene,G=C.index;
function I(O,K){pv.Mark.scene=O;
C.index=K;
if(!O){return 
}var N=O.mark,Q=N,M=[];
do{M.push(Q);
H.push(O[K].data);
Q.index=K;
Q.scene=O;
K=O.parentIndex;
O=O.parent
}while(Q=Q.parent);
for(var L=M.length-1,J=1;
L>0;
L--){Q=M[L];
Q.scale=J;
J*=Q.scene[Q.index].transform.k
}if(N.children){for(var L=0,P=N.children.length;
L<P;
L++){Q=N.children[L];
Q.scene=N.scene[N.index].children[L];
Q.scale=J
}}}function A(M,J){if(!M){return 
}var L=M.mark,O;
if(L.children){for(var K=0,N=L.children.length;
K<N;
K++){O=L.children[K];
delete O.scene;
delete O.scale
}}O=L;
do{H.pop();
if(O.parent){delete O.scene;
delete O.scale
}delete O.index
}while(O=O.parent)
}A(D,G);
I(B,F);
try{E.apply(this,H)
}finally{A(B,F);
I(D,G)
}};
pv.Mark.dispatch=function(D,F,C){var A=F.mark,E=F.parent,B=A.$handlers[D];
if(!B){return E&&pv.Mark.dispatch(D,E,F.parentIndex)
}A.context(F,C,function(){A=B.apply(A,pv.Mark.stack);
if(A&&A.render){A.render()
}});
return true
};
pv.Anchor=function(A){pv.Mark.call(this);
this.target=A;
this.parent=A.parent
};
pv.Anchor.prototype=pv.extend(pv.Mark).property("name",String);
pv.Anchor.prototype.extend=function(A){this.proto=A;
return this
};
pv.Area=function(){pv.Mark.call(this)
};
pv.Area.prototype=pv.extend(pv.Mark).property("width",Number).property("height",Number).property("lineWidth",Number).property("strokeStyle",pv.color).property("fillStyle",pv.color).property("segmented",Boolean).property("interpolate",String).property("tension",Number);
pv.Area.prototype.type="area";
pv.Area.prototype.defaults=new pv.Area().extend(pv.Mark.prototype.defaults).lineWidth(1.5).fillStyle(pv.Colors.category20().by(pv.parent)).interpolate("linear").tension(0.7);
pv.Area.prototype.buildImplied=function(A){if(A.height==null){A.height=0
}if(A.width==null){A.width=0
}pv.Mark.prototype.buildImplied.call(this,A)
};
pv.Area.fixed={lineWidth:1,lineJoin:1,strokeStyle:1,fillStyle:1,segmented:1,interpolate:1,tension:1};
pv.Area.prototype.bind=function(){pv.Mark.prototype.bind.call(this);
var C=this.binds,E=C.required,A=C.optional;
for(var B=0,F=A.length;
B<F;
B++){var D=A[B];
D.fixed=D.name in pv.Area.fixed;
if(D.name=="segmented"){E.push(D);
A.splice(B,1);
B--;
F--
}}this.binds.$required=E;
this.binds.$optional=A
};
pv.Area.prototype.buildInstance=function(C){var B=this.binds;
if(this.index){var D=B.fixed;
if(!D){D=B.fixed=[];
function E(H){return !H.fixed||(D.push(H),false)
}B.required=B.required.filter(E);
if(!this.scene[0].segmented){B.optional=B.optional.filter(E)
}}for(var A=0,G=D.length;
A<G;
A++){var F=D[A].name;
C[F]=this.scene[0][F]
}}else{B.required=B.$required;
B.optional=B.$optional;
B.fixed=null
}pv.Mark.prototype.buildInstance.call(this,C)
};
pv.Area.prototype.anchor=function(A){return pv.Mark.prototype.anchor.call(this,A).interpolate(function(){return this.scene.target[this.index].interpolate
}).eccentricity(function(){return this.scene.target[this.index].eccentricity
}).tension(function(){return this.scene.target[this.index].tension
})
};
pv.Bar=function(){pv.Mark.call(this)
};
pv.Bar.prototype=pv.extend(pv.Mark).property("width",Number).property("height",Number).property("lineWidth",Number).property("strokeStyle",pv.color).property("fillStyle",pv.color);
pv.Bar.prototype.type="bar";
pv.Bar.prototype.defaults=new pv.Bar().extend(pv.Mark.prototype.defaults).lineWidth(1.5).fillStyle(pv.Colors.category20().by(pv.parent));
pv.Dot=function(){pv.Mark.call(this)
};
pv.Dot.prototype=pv.extend(pv.Mark).property("size",Number).property("radius",Number).property("shape",String).property("angle",Number).property("lineWidth",Number).property("strokeStyle",pv.color).property("fillStyle",pv.color);
pv.Dot.prototype.type="dot";
pv.Dot.prototype.defaults=new pv.Dot().extend(pv.Mark.prototype.defaults).size(20).shape("circle").lineWidth(1.5).strokeStyle(pv.Colors.category10().by(pv.parent));
pv.Dot.prototype.anchor=function(A){return pv.Mark.prototype.anchor.call(this,A).left(function(){var B=this.scene.target[this.index];
switch(this.name()){case"bottom":case"top":case"center":return B.left;
case"left":return null
}return B.left+B.radius
}).right(function(){var B=this.scene.target[this.index];
return this.name()=="left"?B.right+B.radius:null
}).top(function(){var B=this.scene.target[this.index];
switch(this.name()){case"left":case"right":case"center":return B.top;
case"top":return null
}return B.top+B.radius
}).bottom(function(){var B=this.scene.target[this.index];
return this.name()=="top"?B.bottom+B.radius:null
}).textAlign(function(){switch(this.name()){case"left":return"right";
case"bottom":case"top":case"center":return"center"
}return"left"
}).textBaseline(function(){switch(this.name()){case"right":case"left":case"center":return"middle";
case"bottom":return"top"
}return"bottom"
})
};
pv.Dot.prototype.buildImplied=function(A){if(A.radius==null){A.radius=Math.sqrt(A.size)
}else{if(A.size==null){A.size=A.radius*A.radius
}}pv.Mark.prototype.buildImplied.call(this,A)
};
pv.Label=function(){pv.Mark.call(this)
};
pv.Label.prototype=pv.extend(pv.Mark).property("text",String).property("font",String).property("textAngle",Number).property("textStyle",pv.color).property("textAlign",String).property("textBaseline",String).property("textMargin",Number).property("textDecoration",String).property("textShadow",String);
pv.Label.prototype.type="label";
pv.Label.prototype.defaults=new pv.Label().extend(pv.Mark.prototype.defaults).events("none").text(pv.identity).font("10px sans-serif").textAngle(0).textStyle("black").textAlign("left").textBaseline("bottom").textMargin(3);
pv.Line=function(){pv.Mark.call(this)
};
pv.Line.prototype=pv.extend(pv.Mark).property("lineWidth",Number).property("lineJoin",String).property("strokeStyle",pv.color).property("fillStyle",pv.color).property("segmented",Boolean).property("interpolate",String).property("eccentricity",Number).property("tension",Number);
pv.Line.prototype.type="line";
pv.Line.prototype.defaults=new pv.Line().extend(pv.Mark.prototype.defaults).lineJoin("miter").lineWidth(1.5).strokeStyle(pv.Colors.category10().by(pv.parent)).interpolate("linear").eccentricity(0).tension(0.7);
pv.Line.prototype.bind=pv.Area.prototype.bind;
pv.Line.prototype.buildInstance=pv.Area.prototype.buildInstance;
pv.Line.prototype.anchor=function(A){return pv.Area.prototype.anchor.call(this,A).textAlign(function(B){switch(this.name()){case"left":return"right";
case"bottom":case"top":case"center":return"center";
case"right":return"left"
}}).textBaseline(function(B){switch(this.name()){case"right":case"left":case"center":return"middle";
case"top":return"bottom";
case"bottom":return"top"
}})
};
pv.Rule=function(){pv.Mark.call(this)
};
pv.Rule.prototype=pv.extend(pv.Mark).property("width",Number).property("height",Number).property("lineWidth",Number).property("strokeStyle",pv.color);
pv.Rule.prototype.type="rule";
pv.Rule.prototype.defaults=new pv.Rule().extend(pv.Mark.prototype.defaults).lineWidth(1).strokeStyle("black").antialias(false);
pv.Rule.prototype.anchor=pv.Line.prototype.anchor;
pv.Rule.prototype.buildImplied=function(D){var B=D.left,E=D.right,C=D.top,A=D.bottom;
if((D.width!=null)||((B==null)&&(E==null))||((E!=null)&&(B!=null))){D.height=0
}else{D.width=0
}pv.Mark.prototype.buildImplied.call(this,D)
};
pv.Panel=function(){pv.Bar.call(this);
this.children=[];
this.root=this;
this.$dom=pv.$&&pv.$.s
};
pv.Panel.prototype=pv.extend(pv.Bar).property("transform").property("overflow",String).property("canvas",function(A){return(typeof A=="string")?document.getElementById(A):A
});
pv.Panel.prototype.type="panel";
pv.Panel.prototype.defaults=new pv.Panel().extend(pv.Bar.prototype.defaults).fillStyle(null).overflow("visible");
pv.Panel.prototype.anchor=function(B){var A=pv.Bar.prototype.anchor.call(this,B);
A.parent=this;
return A
};
pv.Panel.prototype.add=function(A){var B=new A();
B.parent=this;
B.root=this.root;
B.childIndex=this.children.length;
this.children.push(B);
return B
};
pv.Panel.prototype.bind=function(){pv.Mark.prototype.bind.call(this);
for(var A=0;
A<this.children.length;
A++){this.children[A].bind()
}};
pv.Panel.prototype.buildInstance=function(B){pv.Bar.prototype.buildInstance.call(this,B);
if(!B.visible){return 
}if(!B.children){B.children=[]
}var C=this.scale*B.transform.k,E,D=this.children.length;
pv.Mark.prototype.index=-1;
for(var A=0;
A<D;
A++){E=this.children[A];
E.scene=B.children[A];
E.scale=C;
E.build()
}for(var A=0;
A<D;
A++){E=this.children[A];
B.children[A]=E.scene;
delete E.scene;
delete E.scale
}B.children.length=D
};
pv.Panel.prototype.buildImplied=function(D){if(!this.parent){var F=D.canvas;
if(F){if(F.$panel!=this){F.$panel=this;
while(F.lastChild){F.removeChild(F.lastChild)
}}var A,C;
if(D.width==null){A=parseFloat(pv.css(F,"width"));
D.width=A-D.left-D.right
}if(D.height==null){C=parseFloat(pv.css(F,"height"));
D.height=C-D.top-D.bottom
}}else{var B=this.$canvas||(this.$canvas=[]);
if(!(F=B[this.index])){F=B[this.index]=document.createElement("span");
if(this.$dom){this.$dom.parentNode.insertBefore(F,this.$dom)
}else{var E=document.body;
while(E.lastChild&&E.lastChild.tagName){E=E.lastChild
}if(E!=document.body){E=E.parentNode
}E.appendChild(F)
}}}D.canvas=F
}if(!D.transform){D.transform=pv.Transform.identity
}pv.Mark.prototype.buildImplied.call(this,D)
};
pv.Image=function(){pv.Bar.call(this)
};
pv.Image.prototype=pv.extend(pv.Bar).property("url",String).property("imageWidth",Number).property("imageHeight",Number);
pv.Image.prototype.type="image";
pv.Image.prototype.defaults=new pv.Image().extend(pv.Bar.prototype.defaults).fillStyle(null);
pv.Image.prototype.image=function(A){this.$image=function(){var B=A.apply(this,arguments);
return B==null?pv.Color.transparent:typeof B=="string"?pv.color(B):B
};
return this
};
pv.Image.prototype.bind=function(){pv.Bar.prototype.bind.call(this);
var A=this.binds,B=this;
do{A.image=B.$image
}while(!A.image&&(B=B.proto))
};
pv.Image.prototype.buildImplied=function(K){pv.Bar.prototype.buildImplied.call(this,K);
if(!K.visible){return 
}if(K.imageWidth==null){K.imageWidth=K.width
}if(K.imageHeight==null){K.imageHeight=K.height
}if((K.url==null)&&this.binds.image){var C=this.$canvas||(this.$canvas=document.createElement("canvas")),B=C.getContext("2d"),J=K.imageWidth,F=K.imageHeight,H=pv.Mark.stack,E;
C.width=J;
C.height=F;
E=(K.image=B.createImageData(J,F)).data;
H.unshift(null,null);
for(var G=0,A=0;
G<F;
G++){H[1]=G;
for(var I=0;
I<J;
I++){H[0]=I;
var D=this.binds.image.apply(this,H);
E[A++]=D.r;
E[A++]=D.g;
E[A++]=D.b;
E[A++]=255*D.a
}}H.splice(0,2)
}};
pv.Wedge=function(){pv.Mark.call(this)
};
pv.Wedge.prototype=pv.extend(pv.Mark).property("startAngle",Number).property("endAngle",Number).property("angle",Number).property("innerRadius",Number).property("outerRadius",Number).property("lineWidth",Number).property("strokeStyle",pv.color).property("fillStyle",pv.color);
pv.Wedge.prototype.type="wedge";
pv.Wedge.prototype.defaults=new pv.Wedge().extend(pv.Mark.prototype.defaults).startAngle(function(){var A=this.sibling();
return A?A.endAngle:-Math.PI/2
}).innerRadius(0).lineWidth(1.5).strokeStyle(null).fillStyle(pv.Colors.category20().by(pv.index));
pv.Wedge.prototype.midRadius=function(){return(this.innerRadius()+this.outerRadius())/2
};
pv.Wedge.prototype.midAngle=function(){return(this.startAngle()+this.endAngle())/2
};
pv.Wedge.prototype.anchor=function(C){function B(E){return E.innerRadius||E.angle<2*Math.PI
}function A(E){return(E.innerRadius+E.outerRadius)/2
}function D(E){return(E.startAngle+E.endAngle)/2
}return pv.Mark.prototype.anchor.call(this,C).left(function(){var E=this.scene.target[this.index];
if(B(E)){switch(this.name()){case"outer":return E.left+E.outerRadius*Math.cos(D(E));
case"inner":return E.left+E.innerRadius*Math.cos(D(E));
case"start":return E.left+A(E)*Math.cos(E.startAngle);
case"center":return E.left+A(E)*Math.cos(D(E));
case"end":return E.left+A(E)*Math.cos(E.endAngle)
}}return E.left
}).top(function(){var E=this.scene.target[this.index];
if(B(E)){switch(this.name()){case"outer":return E.top+E.outerRadius*Math.sin(D(E));
case"inner":return E.top+E.innerRadius*Math.sin(D(E));
case"start":return E.top+A(E)*Math.sin(E.startAngle);
case"center":return E.top+A(E)*Math.sin(D(E));
case"end":return E.top+A(E)*Math.sin(E.endAngle)
}}return E.top
}).textAlign(function(){var E=this.scene.target[this.index];
if(B(E)){switch(this.name()){case"outer":return pv.Wedge.upright(D(E))?"right":"left";
case"inner":return pv.Wedge.upright(D(E))?"left":"right"
}}return"center"
}).textBaseline(function(){var E=this.scene.target[this.index];
if(B(E)){switch(this.name()){case"start":return pv.Wedge.upright(E.startAngle)?"top":"bottom";
case"end":return pv.Wedge.upright(E.endAngle)?"bottom":"top"
}}return"middle"
}).textAngle(function(){var F=this.scene.target[this.index],E=0;
if(B(F)){switch(this.name()){case"center":case"inner":case"outer":E=D(F);
break;
case"start":E=F.startAngle;
break;
case"end":E=F.endAngle;
break
}}return pv.Wedge.upright(E)?E:(E+Math.PI)
})
};
pv.Wedge.upright=function(A){A=A%(2*Math.PI);
A=(A<0)?(2*Math.PI+A):A;
return(A<Math.PI/2)||(A>=3*Math.PI/2)
};
pv.Wedge.prototype.buildImplied=function(A){if(A.angle==null){A.angle=A.endAngle-A.startAngle
}else{if(A.endAngle==null){A.endAngle=A.startAngle+A.angle
}}pv.Mark.prototype.buildImplied.call(this,A)
};
pv.simulation=function(A){return new pv.Simulation(A)
};
pv.Simulation=function(B){for(var A=0;
A<B.length;
A++){this.particle(B[A])
}};
pv.Simulation.prototype.particle=function(A){A.next=this.particles;
if(isNaN(A.px)){A.px=A.x
}if(isNaN(A.py)){A.py=A.y
}if(isNaN(A.fx)){A.fx=0
}if(isNaN(A.fy)){A.fy=0
}this.particles=A;
return this
};
pv.Simulation.prototype.force=function(A){A.next=this.forces;
this.forces=A;
return this
};
pv.Simulation.prototype.constraint=function(A){A.next=this.constraints;
this.constraints=A;
return this
};
pv.Simulation.prototype.stabilize=function(E){var D;
if(!arguments.length){E=3
}for(var A=0;
A<E;
A++){var B=new pv.Quadtree(this.particles);
for(D=this.constraints;
D;
D=D.next){D.apply(this.particles,B)
}}for(var C=this.particles;
C;
C=C.next){C.px=C.x;
C.py=C.y
}return this
};
pv.Simulation.prototype.step=function(){var E,D,F;
for(E=this.particles;
E;
E=E.next){var B=E.px,A=E.py;
E.px=E.x;
E.py=E.y;
E.x+=E.vx=((E.x-B)+E.fx);
E.y+=E.vy=((E.y-A)+E.fy)
}var C=new pv.Quadtree(this.particles);
for(F=this.constraints;
F;
F=F.next){F.apply(this.particles,C)
}for(E=this.particles;
E;
E=E.next){E.fx=E.fy=0
}for(D=this.forces;
D;
D=D.next){D.apply(this.particles,C)
}};
pv.Quadtree=function(H){var B;
var C=Number.POSITIVE_INFINITY,F=C,A=Number.NEGATIVE_INFINITY,E=A;
for(B=H;
B;
B=B.next){if(B.x<C){C=B.x
}if(B.y<F){F=B.y
}if(B.x>A){A=B.x
}if(B.y>E){E=B.y
}}var J=A-C,I=E-F;
if(J>I){E=F+J
}else{A=C+I
}this.xMin=C;
this.yMin=F;
this.xMax=A;
this.yMax=E;
function G(Q,P,M,O,L,N){if(isNaN(P.x)||isNaN(P.y)){return 
}if(Q.leaf){if(Q.p){if((Math.abs(Q.p.x-P.x)+Math.abs(Q.p.y-P.y))<0.01){D(Q,P,M,O,L,N)
}else{var K=Q.p;
Q.p=null;
D(Q,K,M,O,L,N);
D(Q,P,M,O,L,N)
}}else{Q.p=P
}}else{D(Q,P,M,O,L,N)
}}function D(O,M,N,S,L,Q){var R=(N+L)*0.5,P=(S+Q)*0.5,T=M.x>=R,K=M.y>=P;
O.leaf=false;
switch((K<<1)+T){case 0:O=O.c1||(O.c1=new pv.Quadtree.Node());
break;
case 1:O=O.c2||(O.c2=new pv.Quadtree.Node());
break;
case 2:O=O.c3||(O.c3=new pv.Quadtree.Node());
break;
case 3:O=O.c4||(O.c4=new pv.Quadtree.Node());
break
}if(T){N=R
}else{L=R
}if(K){S=P
}else{Q=P
}G(O,M,N,S,L,Q)
}this.root=new pv.Quadtree.Node();
for(B=H;
B;
B=B.next){G(this.root,B,C,F,A,E)
}};
pv.Quadtree.Node=function(){this.leaf=true;
this.c1=null;
this.c2=null;
this.c3=null;
this.c4=null;
this.p=null
};
pv.Force={};
pv.Force.charge=function(D){var E=2,I=1/E,F=500,G=1/F,B=0.9,A={};
if(!arguments.length){D=-40
}A.constant=function(J){if(arguments.length){D=Number(J);
return A
}return D
};
A.domain=function(K,J){if(arguments.length){E=Number(K);
I=1/E;
F=Number(J);
G=1/F;
return A
}return[E,F]
};
A.theta=function(J){if(arguments.length){B=Number(J);
return A
}return B
};
function H(M){var J=0,L=0;
M.cn=0;
function K(N){H(N);
M.cn+=N.cn;
J+=N.cn*N.cx;
L+=N.cn*N.cy
}if(!M.leaf){if(M.c1){K(M.c1)
}if(M.c2){K(M.c2)
}if(M.c3){K(M.c3)
}if(M.c4){K(M.c4)
}}if(M.p){M.cn+=D;
J+=D*M.p.x;
L+=D*M.p.y
}M.cx=J/M.cn;
M.cy=L/M.cn
}function C(M,K,L,U,J,S){var W=M.cx-K.x,V=M.cy-K.y,P=1/Math.sqrt(W*W+V*V);
if((M.leaf&&(M.p!=K))||((J-L)*P<B)){if(P<G){return 
}if(P>I){P=I
}var Q=M.cn*P*P*P,O=W*Q,N=V*Q;
K.fx+=O;
K.fy+=N
}else{if(!M.leaf){var T=(L+J)*0.5,R=(U+S)*0.5;
if(M.c1){C(M.c1,K,L,U,T,R)
}if(M.c2){C(M.c2,K,T,U,J,R)
}if(M.c3){C(M.c3,K,L,R,T,S)
}if(M.c4){C(M.c4,K,T,R,J,S)
}if(P<G){return 
}if(P>I){P=I
}if(M.p&&(M.p!=K)){var Q=D*P*P*P,O=W*Q,N=V*Q;
K.fx+=O;
K.fy+=N
}}}}A.apply=function(J,K){H(K.root);
for(var L=J;
L;
L=L.next){C(K.root,L,K.xMin,K.yMin,K.xMax,K.yMax)
}};
return A
};
pv.Force.drag=function(A){var B={};
if(!arguments.length){A=0.1
}B.constant=function(C){if(arguments.length){A=C;
return B
}return A
};
B.apply=function(C){if(A){for(var D=C;
D;
D=D.next){D.fx-=A*D.vx;
D.fy-=A*D.vy
}}};
return B
};
pv.Force.spring=function(C){var F=0.1,A=20,B,D,E={};
if(!arguments.length){C=0.1
}E.links=function(G){if(arguments.length){B=G;
D=G.map(function(H){return 1/Math.sqrt(Math.max(H.sourceNode.linkDegree,H.targetNode.linkDegree))
});
return E
}return B
};
E.constant=function(G){if(arguments.length){C=Number(G);
return E
}return C
};
E.damping=function(G){if(arguments.length){F=Number(G);
return E
}return F
};
E.length=function(G){if(arguments.length){A=Number(G);
return E
}return A
};
E.apply=function(Q){for(var K=0;
K<B.length;
K++){var O=B[K].sourceNode,M=B[K].targetNode,S=O.x-M.x,R=O.y-M.y,L=Math.sqrt(S*S+R*R),P=L?(1/L):1,N=C*D[K],J=F*D[K],G=(N*(L-A)+J*(S*(O.vx-M.vx)+R*(O.vy-M.vy))*P)*P,I=-G*(L?S:(0.01*(0.5-Math.random()))),H=-G*(L?R:(0.01*(0.5-Math.random())));
O.fx+=I;
O.fy+=H;
M.fx-=I;
M.fy-=H
}};
return E
};
pv.Constraint={};
pv.Constraint.collision=function(H){var D=1,E,G,B,F,A,C={};
if(!arguments.length){E=10
}C.repeat=function(J){if(arguments.length){D=Number(J);
return C
}return D
};
function I(N,L,M,W,K,U){if(!N.leaf){var V=(M+K)*0.5,T=(W+U)*0.5,S=T>B,J=T<A,O=V>G,X=V<F;
if(S){if(N.c1&&O){I(N.c1,L,M,W,V,T)
}if(N.c2&&X){I(N.c2,L,V,W,K,T)
}}if(J){if(N.c3&&O){I(N.c3,L,M,T,V,U)
}if(N.c4&&X){I(N.c4,L,V,T,K,U)
}}}if(N.p&&(N.p!=L)){var Z=L.x-N.p.x,Y=L.y-N.p.y,P=Math.sqrt(Z*Z+Y*Y),R=E+H(N.p);
if(P<R){var Q=(P-R)/P*0.5;
Z*=Q;
Y*=Q;
L.x-=Z;
L.y-=Y;
N.p.x+=Z;
N.p.y+=Y
}}}C.apply=function(M,N){var O,L,J=-Infinity;
for(O=M;
O;
O=O.next){L=H(O);
if(L>J){J=L
}}for(var K=0;
K<D;
K++){for(O=M;
O;
O=O.next){L=(E=H(O))+J;
G=O.x-L;
F=O.x+L;
B=O.y-L;
A=O.y+L;
I(N.root,O,N.xMin,N.yMin,N.xMax,N.yMax)
}}};
return C
};
pv.Constraint.position=function(B){var A=1,C={};
if(!arguments.length){B=function(D){return D.fix
}
}C.alpha=function(D){if(arguments.length){A=Number(D);
return C
}return A
};
C.apply=function(E){for(var F=E;
F;
F=F.next){var D=B(F);
if(D){F.x+=(D.x-F.x)*A;
F.y+=(D.y-F.y)*A;
F.fx=F.fy=F.vx=F.vy=0
}}};
return C
};
pv.Constraint.bound=function(){var B={},A,C;
B.x=function(E,D){if(arguments.length){A={min:Math.min(E,D),max:Math.max(E,D)};
return this
}return A
};
B.y=function(E,D){if(arguments.length){C={min:Math.min(E,D),max:Math.max(E,D)};
return this
}return C
};
B.apply=function(D){if(A){for(var E=D;
E;
E=E.next){E.x=E.x<A.min?A.min:(E.x>A.max?A.max:E.x)
}}if(C){for(var E=D;
E;
E=E.next){E.y=E.y<C.min?C.min:(E.y>C.max?C.max:E.y)
}}};
return B
};
pv.Layout=function(){pv.Panel.call(this)
};
pv.Layout.prototype=pv.extend(pv.Panel);
pv.Layout.prototype.property=function(B,A){if(!this.hasOwnProperty("properties")){this.properties=pv.extend(this.properties)
}this.properties[B]=true;
this.propertyMethod(B,false,pv.Mark.cast[B]=A);
return this
};
pv.Layout.Network=function(){pv.Layout.call(this);
var A=this;
this.$id=pv.id();
(this.node=new pv.Mark().data(function(){return A.nodes()
}).strokeStyle("#1f77b4").fillStyle("#fff").left(function(B){return B.x
}).top(function(B){return B.y
})).parent=this;
this.link=new pv.Mark().extend(this.node).data(function(B){return[B.sourceNode,B.targetNode]
}).fillStyle(null).lineWidth(function(C,B){return B.linkValue*1.5
}).strokeStyle("rgba(0,0,0,.2)");
this.link.add=function(B){return A.add(pv.Panel).data(function(){return A.links()
}).add(B).extend(this)
};
(this.label=new pv.Mark().extend(this.node).textMargin(7).textBaseline("middle").text(function(B){return B.nodeName||B.nodeValue
}).textAngle(function(C){var B=C.midAngle;
return pv.Wedge.upright(B)?B:(B+Math.PI)
}).textAlign(function(B){return pv.Wedge.upright(B.midAngle)?"left":"right"
})).parent=this
};
pv.Layout.Network.prototype=pv.extend(pv.Layout).property("nodes",function(A){return A.map(function(C,B){if(typeof C!="object"){C={nodeValue:C}
}C.index=B;
return C
})
}).property("links",function(A){return A.map(function(B){if(isNaN(B.linkValue)){B.linkValue=isNaN(B.value)?1:B.value
}return B
})
});
pv.Layout.Network.prototype.reset=function(){this.$id=pv.id();
return this
};
pv.Layout.Network.prototype.buildProperties=function(B,A){if((B.$id||0)<this.$id){pv.Layout.prototype.buildProperties.call(this,B,A)
}};
pv.Layout.Network.prototype.buildImplied=function(A){pv.Layout.prototype.buildImplied.call(this,A);
if(A.$id>=this.$id){return true
}A.$id=this.$id;
A.nodes.forEach(function(B){B.linkDegree=0
});
A.links.forEach(function(C){var B=C.linkValue;
(C.sourceNode||(C.sourceNode=A.nodes[C.source])).linkDegree+=B;
(C.targetNode||(C.targetNode=A.nodes[C.target])).linkDegree+=B
})
};
pv.Layout.Hierarchy=function(){pv.Layout.Network.call(this);
this.link.strokeStyle("#ccc")
};
pv.Layout.Hierarchy.prototype=pv.extend(pv.Layout.Network);
pv.Layout.Hierarchy.prototype.buildImplied=function(A){if(!A.links){A.links=pv.Layout.Hierarchy.links.call(this)
}pv.Layout.Network.prototype.buildImplied.call(this,A)
};
pv.Layout.Hierarchy.links=function(){return this.nodes().filter(function(A){return A.parentNode
}).map(function(A){return{sourceNode:A,targetNode:A.parentNode,linkValue:1}
})
};
pv.Layout.Hierarchy.NodeLink={buildImplied:function(N){var A=N.nodes,E=N.orient,B=/^(top|bottom)$/.test(E),M=N.width,H=N.height;
if(E=="radial"){var I=N.innerRadius,G=N.outerRadius;
if(I==null){I=0
}if(G==null){G=Math.min(M,H)/2
}}function J(O){return O.parentNode?(O.depth*(G-I)+I):0
}function D(O){return(O.parentNode?(O.breadth-0.25)*2*Math.PI:0)
}function L(O){switch(E){case"left":return O.depth*M;
case"right":return M-O.depth*M;
case"top":return O.breadth*M;
case"bottom":return M-O.breadth*M;
case"radial":return M/2+J(O)*Math.cos(O.midAngle)
}}function K(O){switch(E){case"left":return O.breadth*H;
case"right":return H-O.breadth*H;
case"top":return O.depth*H;
case"bottom":return H-O.depth*H;
case"radial":return H/2+J(O)*Math.sin(O.midAngle)
}}for(var F=0;
F<A.length;
F++){var C=A[F];
C.midAngle=E=="radial"?D(C):B?Math.PI/2:0;
C.x=L(C);
C.y=K(C);
if(C.firstChild){C.midAngle+=Math.PI
}}}};
pv.Layout.Hierarchy.Fill={constructor:function(){this.node.strokeStyle("#fff").fillStyle("#ccc").width(function(A){return A.dx
}).height(function(A){return A.dy
}).innerRadius(function(A){return A.innerRadius
}).outerRadius(function(A){return A.outerRadius
}).startAngle(function(A){return A.startAngle
}).angle(function(A){return A.angle
});
this.label.textAlign("center").left(function(A){return A.x+(A.dx/2)
}).top(function(A){return A.y+(A.dy/2)
});
delete this.link
},buildImplied:function(J){var L=J.nodes,F=J.orient,R=/^(top|bottom)$/.test(F),H=J.width,P=J.height,S=-L[0].minDepth;
if(F=="radial"){var A=J.innerRadius,C=J.outerRadius;
if(A==null){A=0
}if(A){S*=2
}if(C==null){C=Math.min(H,P)/2
}}function T(V,U){return(V+U)/(1+U)
}function E(U){switch(F){case"left":return T(U.minDepth,S)*H;
case"right":return(1-T(U.maxDepth,S))*H;
case"top":return U.minBreadth*H;
case"bottom":return(1-U.maxBreadth)*H;
case"radial":return H/2
}}function D(U){switch(F){case"left":return U.minBreadth*P;
case"right":return(1-U.maxBreadth)*P;
case"top":return T(U.minDepth,S)*P;
case"bottom":return(1-T(U.maxDepth,S))*P;
case"radial":return P/2
}}function I(U){switch(F){case"left":case"right":return(U.maxDepth-U.minDepth)/(1+S)*H;
case"top":case"bottom":return(U.maxBreadth-U.minBreadth)*H;
case"radial":return U.parentNode?(U.innerRadius+U.outerRadius)*Math.cos(U.midAngle):0
}}function G(U){switch(F){case"left":case"right":return(U.maxBreadth-U.minBreadth)*P;
case"top":case"bottom":return(U.maxDepth-U.minDepth)/(1+S)*P;
case"radial":return U.parentNode?(U.innerRadius+U.outerRadius)*Math.sin(U.midAngle):0
}}function B(U){return Math.max(0,T(U.minDepth,S/2))*(C-A)+A
}function M(U){return T(U.maxDepth,S/2)*(C-A)+A
}function N(U){return(U.parentNode?U.minBreadth-0.25:0)*2*Math.PI
}function Q(U){return(U.parentNode?U.maxBreadth-U.minBreadth:1)*2*Math.PI
}for(var O=0;
O<L.length;
O++){var K=L[O];
K.x=E(K);
K.y=D(K);
if(F=="radial"){K.innerRadius=B(K);
K.outerRadius=M(K);
K.startAngle=N(K);
K.angle=Q(K);
K.midAngle=K.startAngle+K.angle/2
}else{K.midAngle=R?-Math.PI/2:0
}K.dx=I(K);
K.dy=G(K)
}}};
pv.Layout.Grid=function(){pv.Layout.call(this);
var A=this;
(this.cell=new pv.Mark().data(function(){return A.scene[A.index].$grid
}).width(function(){return A.width()/A.cols()
}).height(function(){return A.height()/A.rows()
}).left(function(){return this.width()*(this.index%A.cols())
}).top(function(){return this.height()*Math.floor(this.index/A.cols())
})).parent=this
};
pv.Layout.Grid.prototype=pv.extend(pv.Layout).property("rows").property("cols");
pv.Layout.Grid.prototype.defaults=new pv.Layout.Grid().extend(pv.Layout.prototype.defaults).rows(1).cols(1);
pv.Layout.Grid.prototype.buildImplied=function(A){pv.Layout.prototype.buildImplied.call(this,A);
var B=A.rows,C=A.cols;
if(typeof C=="object"){B=pv.transpose(C)
}if(typeof B=="object"){A.$grid=pv.blend(B);
A.rows=B.length;
A.cols=B[0]?B[0].length:0
}else{A.$grid=pv.repeat([A.data],B*C)
}};
pv.Layout.Stack=function(){pv.Layout.call(this);
var E=this,C=function(){return null
},F={t:C,l:C,r:C,b:C,w:C,h:C},A,D=E.buildImplied;
function B(G){return function(){return F[G](this.parent.index,this.index)
}
}this.buildImplied=function(V){D.call(this,V);
var u=V.layers,a=u.length,c,Q=V.orient,r=/^(top|bottom)\b/.test(Q),q=this.parent[r?"height":"width"](),P=[],O=[],R=[];
var J=pv.Mark.stack,Z={parent:{parent:this}};
J.unshift(null);
A=[];
for(var l=0;
l<a;
l++){R[l]=[];
O[l]=[];
Z.parent.index=l;
J[0]=u[l];
A[l]=this.$values.apply(Z.parent,J);
if(!l){c=A[l].length
}J.unshift(null);
for(var f=0;
f<c;
f++){J[0]=A[l][f];
Z.index=f;
if(!l){P[f]=this.$x.apply(Z,J)
}R[l][f]=this.$y.apply(Z,J)
}J.shift()
}J.shift();
var K;
switch(V.order){case"inside-out":var g=R.map(function(h){return pv.max.index(h)
}),t=pv.range(a).sort(function(i,h){return g[i]-g[h]
}),T=R.map(function(h){return pv.sum(h)
}),X=0,M=0,L=[],b=[];
for(var l=0;
l<a;
l++){var f=t[l];
if(X<M){X+=T[f];
L.push(f)
}else{M+=T[f];
b.push(f)
}}K=b.reverse().concat(L);
break;
case"reverse":K=pv.range(a-1,-1,-1);
break;
default:K=pv.range(a);
break
}switch(V.offset){case"silohouette":for(var f=0;
f<c;
f++){var Z=0;
for(var l=0;
l<a;
l++){Z+=R[l][f]
}O[K[0]][f]=(q-Z)/2
}break;
case"wiggle":var Z=0;
for(var l=0;
l<a;
l++){Z+=R[l][0]
}O[K[0]][0]=Z=(q-Z)/2;
for(var f=1;
f<c;
f++){var I=0,H=0,S=P[f]-P[f-1];
for(var l=0;
l<a;
l++){I+=R[l][f]
}for(var l=0;
l<a;
l++){var G=(R[K[l]][f]-R[K[l]][f-1])/(2*S);
for(var d=0;
d<l;
d++){G+=(R[K[d]][f]-R[K[d]][f-1])/S
}H+=G*R[K[l]][f]
}O[K[0]][f]=Z-=I?H/I*S:0
}break;
case"expand":for(var f=0;
f<c;
f++){O[K[0]][f]=0;
var d=0;
for(var l=0;
l<a;
l++){d+=R[l][f]
}if(d){d=q/d;
for(var l=0;
l<a;
l++){R[l][f]*=d
}}else{d=q/a;
for(var l=0;
l<a;
l++){R[l][f]=d
}}}break;
default:for(var f=0;
f<c;
f++){O[K[0]][f]=0
}break
}for(var f=0;
f<c;
f++){var Z=O[K[0]][f];
for(var l=1;
l<a;
l++){Z+=R[K[l-1]][f];
O[K[l]][f]=Z
}}var l=Q.indexOf("-"),N=r?"h":"w",W=l<0?(r?"l":"b"):Q.charAt(l+1),U=Q.charAt(0);
for(var Y in F){F[Y]=C
}F[W]=function(k,h){return P[h]
};
F[U]=function(k,h){return O[k][h]
};
F[N]=function(k,h){return R[k][h]
}
};
this.layer=new pv.Mark().data(function(){return A[this.parent.index]
}).top(B("t")).left(B("l")).right(B("r")).bottom(B("b")).width(B("w")).height(B("h"));
this.layer.add=function(G){return E.add(pv.Panel).data(function(){return E.layers()
}).add(G).extend(this)
}
};
pv.Layout.Stack.prototype=pv.extend(pv.Layout).property("orient",String).property("offset",String).property("order",String).property("layers");
pv.Layout.Stack.prototype.defaults=new pv.Layout.Stack().extend(pv.Layout.prototype.defaults).orient("bottom-left").offset("zero").layers([[]]);
pv.Layout.Stack.prototype.$x=pv.Layout.Stack.prototype.$y=function(){return 0
};
pv.Layout.Stack.prototype.x=function(A){this.$x=pv.functor(A);
return this
};
pv.Layout.Stack.prototype.y=function(A){this.$y=pv.functor(A);
return this
};
pv.Layout.Stack.prototype.$values=pv.identity;
pv.Layout.Stack.prototype.values=function(A){this.$values=pv.functor(A);
return this
};
pv.Layout.Treemap=function(){pv.Layout.Hierarchy.call(this);
this.node.strokeStyle("#fff").fillStyle("rgba(31, 119, 180, .25)").width(function(A){return A.dx
}).height(function(A){return A.dy
});
this.label.visible(function(A){return !A.firstChild
}).left(function(A){return A.x+(A.dx/2)
}).top(function(A){return A.y+(A.dy/2)
}).textAlign("center").textAngle(function(A){return A.dx>A.dy?0:-Math.PI/2
});
(this.leaf=new pv.Mark().extend(this.node).fillStyle(null).strokeStyle(null).visible(function(A){return !A.firstChild
})).parent=this;
delete this.link
};
pv.Layout.Treemap.prototype=pv.extend(pv.Layout.Hierarchy).property("round",Boolean).property("paddingLeft",Number).property("paddingRight",Number).property("paddingTop",Number).property("paddingBottom",Number).property("mode",String).property("order",String);
pv.Layout.Treemap.prototype.defaults=new pv.Layout.Treemap().extend(pv.Layout.Hierarchy.prototype.defaults).mode("squarify").order("ascending");
pv.Layout.Treemap.prototype.padding=function(A){return this.paddingLeft(A).paddingRight(A).paddingTop(A).paddingBottom(A)
};
pv.Layout.Treemap.prototype.$size=function(A){return Number(A.nodeValue)
};
pv.Layout.Treemap.prototype.size=function(A){this.$size=pv.functor(A);
return this
};
pv.Layout.Treemap.prototype.buildImplied=function(O){if(pv.Layout.Hierarchy.prototype.buildImplied.call(this,O)){return 
}var F=this,B=O.nodes,I=B[0],J=pv.Mark.stack,C=O.paddingLeft,L=O.paddingRight,H=O.paddingTop,A=O.paddingBottom,N=function(P){return P.size
},M=O.round?Math.round:Number,E=O.mode;
function K(Y,T,P,W,V,X,S){for(var R=0,U=0;
R<Y.length;
R++){var Q=Y[R];
if(P){Q.x=W+U;
Q.y=V;
U+=Q.dx=M(X*Q.size/T);
Q.dy=S
}else{Q.x=W;
Q.y=V+U;
Q.dx=X;
U+=Q.dy=M(S*Q.size/T)
}}if(Q){if(P){Q.dx+=X-U
}else{Q.dy+=S-U
}}}function G(U,P){var V=-Infinity,Q=Infinity,S=0;
for(var R=0;
R<U.length;
R++){var T=U[R].size;
if(T<Q){Q=T
}if(T>V){V=T
}S+=T
}S=S*S;
P=P*P;
return Math.max(P*V/S,S/(P*Q))
}function D(R,U){var Z=R.x+C,Y=R.y+H,a=R.dx-C-L,W=R.dy-H-A;
if(E!="squarify"){K(R.childNodes,R.size,E=="slice"?true:E=="dice"?false:U&1,Z,Y,a,W);
return 
}var b=[],X=Infinity,S=Math.min(a,W),T=a*W/R.size;
if(R.size<=0){return 
}R.visitBefore(function(c){c.size*=T
});
function V(g){var c=a==S,d=pv.sum(g,N),f=S?M(d/S):0;
K(g,d,c,Z,Y,c?a:f,c?f:W);
if(c){Y+=f;
W-=f
}else{Z+=f;
a-=f
}S=Math.min(a,W);
return c
}var Q=R.childNodes.slice();
while(Q.length){var P=Q[Q.length-1];
if(!P.size){Q.pop();
continue
}b.push(P);
var T=G(b,S);
if(T<=X){Q.pop();
X=T
}else{b.pop();
V(b);
b.length=0;
X=Infinity
}}if(V(b)){for(var U=0;
U<b.length;
U++){b[U].dy+=W
}}else{for(var U=0;
U<b.length;
U++){b[U].dx+=a
}}}J.unshift(null);
I.visitAfter(function(Q,P){Q.depth=P;
Q.x=Q.y=Q.dx=Q.dy=0;
Q.size=Q.firstChild?pv.sum(Q.childNodes,function(R){return R.size
}):F.$size.apply(F,(J[0]=Q,J))
});
J.shift();
switch(O.order){case"ascending":I.sort(function(Q,P){return Q.size-P.size
});
break;
case"descending":I.sort(function(Q,P){return P.size-Q.size
});
break;
case"reverse":I.reverse();
break
}I.x=0;
I.y=0;
I.dx=O.width;
I.dy=O.height;
I.visitBefore(D)
};
pv.Layout.Tree=function(){pv.Layout.Hierarchy.call(this)
};
pv.Layout.Tree.prototype=pv.extend(pv.Layout.Hierarchy).property("group",Number).property("breadth",Number).property("depth",Number).property("orient",String);
pv.Layout.Tree.prototype.defaults=new pv.Layout.Tree().extend(pv.Layout.Hierarchy.prototype.defaults).group(1).breadth(15).depth(60).orient("top");
pv.Layout.Tree.prototype.buildImplied=function(J){if(pv.Layout.Hierarchy.prototype.buildImplied.call(this,J)){return 
}var M=J.nodes,G=J.orient,T=J.depth,I=J.breadth,B=J.group,H=J.width,Q=J.height;
function R(X){var W,Z,V;
if(!X.firstChild){if(W=X.previousSibling){X.prelim=W.prelim+A(X.depth,true)
}}else{W=X.firstChild;
Z=X.lastChild;
V=W;
for(var b=W;
b;
b=b.nextSibling){R(b);
V=L(b,V)
}F(X);
var Y=0.5*(W.prelim+Z.prelim);
if(W=X.previousSibling){X.prelim=W.prelim+A(X.depth,true);
X.mod=X.prelim-Y
}else{X.prelim=Y
}}}function N(W,V,X){W.breadth=W.prelim+V;
V+=W.mod;
for(var Y=W.firstChild;
Y;
Y=Y.nextSibling){N(Y,V,X)
}}function L(k,i){var j=k.previousSibling;
if(j){var Y=k,X=k,b=j,Z=k.parentNode.firstChild,d=Y.mod,c=X.mod,g=b.mod,f=Z.mod,h=U(b),V=P(Y);
while(h&&V){b=h;
Y=V;
Z=P(Z);
X=U(X);
X.ancestor=k;
var W=(b.prelim+g)-(Y.prelim+d)+A(b.depth,false);
if(W>0){K(S(b,k,i),k,W);
d+=W;
c+=W
}g+=b.mod;
d+=Y.mod;
f+=Z.mod;
c+=X.mod;
h=U(b);
V=P(Y)
}if(h&&!U(X)){X.thread=h;
X.mod+=g-c
}if(V&&!P(Z)){Z.thread=V;
Z.mod+=d-f;
i=k
}}return i
}function P(V){return V.firstChild||V.thread
}function U(V){return V.lastChild||V.thread
}function K(X,W,V){var Y=W.number-X.number;
W.change-=V/Y;
W.shift+=V;
X.change+=V/Y;
W.prelim+=V;
W.mod+=V
}function F(W){var V=0,Y=0;
for(var X=W.lastChild;
X;
X=X.previousSibling){X.prelim+=V;
X.mod+=V;
Y+=X.change;
V+=X.shift+Y
}}function S(X,W,V){return(X.ancestor.parentNode==W.parentNode)?X.ancestor:V
}function A(W,V){return(V?1:(B+1))/((G=="radial")?W:1)
}var O=M[0];
O.visitAfter(function(V,W){V.ancestor=V;
V.prelim=0;
V.mod=0;
V.change=0;
V.shift=0;
V.number=V.previousSibling?(V.previousSibling.number+1):0;
V.depth=W
});
R(O);
N(O,-O.prelim,0);
function E(V){return(G=="radial")?V.breadth/T:0
}function D(V){switch(G){case"left":return V.depth;
case"right":return H-V.depth;
case"top":case"bottom":return V.breadth+H/2;
case"radial":return H/2+V.depth*Math.cos(E(V))
}}function C(V){switch(G){case"left":case"right":return V.breadth+Q/2;
case"top":return V.depth;
case"bottom":return Q-V.depth;
case"radial":return Q/2+V.depth*Math.sin(E(V))
}}O.visitAfter(function(V){V.breadth*=I;
V.depth*=T;
V.midAngle=E(V);
V.x=D(V);
V.y=C(V);
if(V.firstChild){V.midAngle+=Math.PI
}delete V.breadth;
delete V.depth;
delete V.ancestor;
delete V.prelim;
delete V.mod;
delete V.change;
delete V.shift;
delete V.number;
delete V.thread
})
};
pv.Layout.Indent=function(){pv.Layout.Hierarchy.call(this);
this.link.interpolate("step-after")
};
pv.Layout.Indent.prototype=pv.extend(pv.Layout.Hierarchy).property("depth",Number).property("breadth",Number);
pv.Layout.Indent.prototype.defaults=new pv.Layout.Indent().extend(pv.Layout.Hierarchy.prototype.defaults).depth(15).breadth(15);
pv.Layout.Indent.prototype.buildImplied=function(C){if(pv.Layout.Hierarchy.prototype.buildImplied.call(this,C)){return 
}var B=C.nodes,G=C.breadth,F=C.depth,E=0,D=0;
function A(K,J,H){K.x=E+H++*F;
K.y=D+J++*G;
K.midAngle=0;
for(var I=K.firstChild;
I;
I=I.nextSibling){J=A(I,J,H)
}return J
}A(B[0],1,1)
};
pv.Layout.Pack=function(){pv.Layout.Hierarchy.call(this);
this.node.radius(function(A){return A.radius
}).strokeStyle("rgb(31, 119, 180)").fillStyle("rgba(31, 119, 180, .25)");
this.label.textAlign("center");
delete this.link
};
pv.Layout.Pack.prototype=pv.extend(pv.Layout.Hierarchy).property("spacing",Number).property("order",String);
pv.Layout.Pack.prototype.defaults=new pv.Layout.Pack().extend(pv.Layout.Hierarchy.prototype.defaults).spacing(1).order("ascending");
pv.Layout.Pack.prototype.$radius=function(){return 1
};
pv.Layout.Pack.prototype.size=function(A){this.$radius=typeof A=="function"?function(){return Math.sqrt(A.apply(this,arguments))
}:(A=Math.sqrt(A),function(){return A
});
return this
};
pv.Layout.Pack.prototype.buildImplied=function(L){if(pv.Layout.Hierarchy.prototype.buildImplied.call(this,L)){return 
}var H=this,A=L.nodes,I=A[0];
function F(N){var M=pv.Mark.stack;
M.unshift(null);
for(var O=0,Q=N.length;
O<Q;
O++){var P=N[O];
if(!P.firstChild){P.radius=H.$radius.apply(H,(M[0]=P,M))
}}M.shift()
}function K(O){var M=[];
for(var N=O.firstChild;
N;
N=N.nextSibling){if(N.firstChild){N.radius=K(N)
}N.n=N.p=N;
M.push(N)
}switch(L.order){case"ascending":M.sort(function(Q,P){return Q.radius-P.radius
});
break;
case"descending":M.sort(function(Q,P){return P.radius-Q.radius
});
break;
case"reverse":M.reverse();
break
}return E(M)
}function E(Y){var g=Infinity,o=-Infinity,M=Infinity,S=-Infinity,m,l,h,d,Z;
function O(a){g=Math.min(a.x-a.radius,g);
o=Math.max(a.x+a.radius,o);
M=Math.min(a.y-a.radius,M);
S=Math.max(a.y+a.radius,S)
}function P(j,i){var k=j.n;
j.n=i;
i.p=j;
i.n=k;
k.p=i
}function p(i,c){i.n=c;
c.p=i
}function Q(j,c){var k=c.x-j.x,i=c.y-j.y,n=j.radius+c.radius;
return(n*n-k*k-i*i)>0.001
}m=Y[0];
m.x=-m.radius;
m.y=0;
O(m);
if(Y.length>1){l=Y[1];
l.x=l.radius;
l.y=0;
O(l);
if(Y.length>2){h=Y[2];
D(m,l,h);
O(h);
P(m,h);
m.p=h;
P(h,l);
l=m.n;
for(var f=3;
f<Y.length;
f++){D(m,l,h=Y[f]);
var N=0,T=1,R=1;
for(d=l.n;
d!=l;
d=d.n,T++){if(Q(d,h)){N=1;
break
}}if(N==1){for(Z=m.p;
Z!=d.p;
Z=Z.p,R++){if(Q(Z,h)){if(R<T){N=-1;
d=Z
}break
}}}if(N==0){P(m,h);
l=h;
O(h)
}else{if(N>0){p(m,d);
l=d;
f--
}else{if(N<0){p(d,l);
m=d;
f--
}}}}}}var V=(g+o)/2,U=(M+S)/2,W=0;
for(var f=0;
f<Y.length;
f++){var X=Y[f];
X.x-=V;
X.y-=U;
W=Math.max(W,X.radius+Math.sqrt(X.x*X.x+X.y*X.y))
}return W+L.spacing
}function D(R,P,O){var W=P.radius+O.radius,U=R.radius+O.radius,X=P.x-R.x,V=P.y-R.y,T=Math.sqrt(X*X+V*V),S=(U*U+T*T-W*W)/(2*U*T),M=Math.acos(S),Q=S*U,N=Math.sin(M)*U;
X/=T;
V/=T;
O.x=R.x+Q*X+N*V;
O.y=R.y+Q*V-N*X
}function B(Q,M,P,N){for(var O=Q.firstChild;
O;
O=O.nextSibling){O.x+=Q.x;
O.y+=Q.y;
B(O,M,P,N)
}Q.x=M+N*Q.x;
Q.y=P+N*Q.y;
Q.radius*=N
}F(A);
I.x=0;
I.y=0;
I.radius=K(I);
var J=this.width(),G=this.height(),C=1/Math.max(2*I.radius/J,2*I.radius/G);
B(I,J/2,G/2,C)
};
pv.Layout.Force=function(){pv.Layout.Network.call(this);
this.link.lineWidth(function(B,A){return Math.sqrt(A.linkValue)*1.5
});
this.label.textAlign("center")
};
pv.Layout.Force.prototype=pv.extend(pv.Layout.Network).property("bound",Boolean).property("iterations",Number).property("dragConstant",Number).property("chargeConstant",Number).property("chargeMinDistance",Number).property("chargeMaxDistance",Number).property("chargeTheta",Number).property("springConstant",Number).property("springDamping",Number).property("springLength",Number);
pv.Layout.Force.prototype.defaults=new pv.Layout.Force().extend(pv.Layout.Network.prototype.defaults).dragConstant(0.1).chargeConstant(-40).chargeMinDistance(2).chargeMaxDistance(500).chargeTheta(0.9).springConstant(0.1).springDamping(0.3).springLength(20);
pv.Layout.Force.prototype.buildImplied=function(M){if(pv.Layout.Network.prototype.buildImplied.call(this,M)){var H=M.$force;
if(H){H.next=this.binds.$force;
this.binds.$force=H
}return 
}var I=this,A=M.nodes,L=M.links,E=M.iterations,K=M.width,G=M.height;
for(var F=0,C;
F<A.length;
F++){C=A[F];
if(isNaN(C.x)){C.x=K/2+40*Math.random()-20
}if(isNaN(C.y)){C.y=G/2+40*Math.random()-20
}}var J=pv.simulation(A);
J.force(pv.Force.drag(M.dragConstant));
J.force(pv.Force.charge(M.chargeConstant).domain(M.chargeMinDistance,M.chargeMaxDistance).theta(M.chargeTheta));
J.force(pv.Force.spring(M.springConstant).damping(M.springDamping).length(M.springLength).links(L));
J.constraint(pv.Constraint.position());
if(M.bound){J.constraint(pv.Constraint.bound().x(6,K-6).y(6,G-6))
}function D(N){return N.fix?1:N.vx*N.vx+N.vy*N.vy
}if(E==null){J.step();
J.step();
var B=M.$force=this.binds.$force={next:this.binds.$force,nodes:A,min:0.0001*(L.length+1),sim:J};
if(!this.$timer){this.$timer=setInterval(function(){var N=false;
for(var O=I.binds.$force;
O;
O=O.next){if(pv.max(O.nodes,D)>O.min){O.sim.step();
N=true
}}if(N){I.render()
}},42)
}}else{for(var F=0;
F<E;
F++){J.step()
}}};
pv.Layout.Cluster=function(){pv.Layout.Hierarchy.call(this);
var A,B=this.buildImplied;
this.buildImplied=function(C){B.call(this,C);
A=/^(top|bottom)$/.test(C.orient)?"step-before":/^(left|right)$/.test(C.orient)?"step-after":"linear"
};
this.link.interpolate(function(){return A
})
};
pv.Layout.Cluster.prototype=pv.extend(pv.Layout.Hierarchy).property("group",Number).property("orient",String).property("innerRadius",Number).property("outerRadius",Number);
pv.Layout.Cluster.prototype.defaults=new pv.Layout.Cluster().extend(pv.Layout.Hierarchy.prototype.defaults).group(0).orient("top");
pv.Layout.Cluster.prototype.buildImplied=function(C){if(pv.Layout.Hierarchy.prototype.buildImplied.call(this,C)){return 
}var A=C.nodes[0],F=C.group,H,G,E=0,B=0.5-F/2;
var D=undefined;
A.visitAfter(function(I){if(I.firstChild){I.depth=1+pv.max(I.childNodes,function(J){return J.depth
})
}else{if(F&&(D!=I.parentNode)){D=I.parentNode;
E+=F
}E++;
I.depth=0
}});
H=1/E;
G=1/A.depth;
var D=undefined;
A.visitAfter(function(I){if(I.firstChild){I.breadth=pv.mean(I.childNodes,function(J){return J.breadth
})
}else{if(F&&(D!=I.parentNode)){D=I.parentNode;
B+=F
}I.breadth=H*B++
}I.depth=1-I.depth*G
});
A.visitAfter(function(I){I.minBreadth=I.firstChild?I.firstChild.minBreadth:(I.breadth-H/2);
I.maxBreadth=I.firstChild?I.lastChild.maxBreadth:(I.breadth+H/2)
});
A.visitBefore(function(I){I.minDepth=I.parentNode?I.parentNode.maxDepth:0;
I.maxDepth=I.parentNode?(I.depth+A.depth):(I.minDepth+2*A.depth)
});
A.minDepth=-G;
pv.Layout.Hierarchy.NodeLink.buildImplied.call(this,C)
};
pv.Layout.Cluster.Fill=function(){pv.Layout.Cluster.call(this);
pv.Layout.Hierarchy.Fill.constructor.call(this)
};
pv.Layout.Cluster.Fill.prototype=pv.extend(pv.Layout.Cluster);
pv.Layout.Cluster.Fill.prototype.buildImplied=function(A){if(pv.Layout.Cluster.prototype.buildImplied.call(this,A)){return 
}pv.Layout.Hierarchy.Fill.buildImplied.call(this,A)
};
pv.Layout.Partition=function(){pv.Layout.Hierarchy.call(this)
};
pv.Layout.Partition.prototype=pv.extend(pv.Layout.Hierarchy).property("order",String).property("orient",String).property("innerRadius",Number).property("outerRadius",Number);
pv.Layout.Partition.prototype.defaults=new pv.Layout.Partition().extend(pv.Layout.Hierarchy.prototype.defaults).orient("top");
pv.Layout.Partition.prototype.$size=function(){return 1
};
pv.Layout.Partition.prototype.size=function(A){this.$size=A;
return this
};
pv.Layout.Partition.prototype.buildImplied=function(C){if(pv.Layout.Hierarchy.prototype.buildImplied.call(this,C)){return 
}var D=this,B=C.nodes[0],A=pv.Mark.stack,F=0;
A.unshift(null);
B.visitAfter(function(H,G){if(G>F){F=G
}H.size=H.firstChild?pv.sum(H.childNodes,function(I){return I.size
}):D.$size.apply(D,(A[0]=H,A))
});
A.shift();
switch(C.order){case"ascending":B.sort(function(H,G){return H.size-G.size
});
break;
case"descending":B.sort(function(G,H){return H.size-G.size
});
break
}var E=1/F;
B.minBreadth=0;
B.breadth=0.5;
B.maxBreadth=1;
B.visitBefore(function(J){var G=J.minBreadth,H=J.maxBreadth-G;
for(var I=J.firstChild;
I;
I=I.nextSibling){I.minBreadth=G;
I.maxBreadth=G+=(I.size/J.size)*H;
I.breadth=(G+I.minBreadth)/2
}});
B.visitAfter(function(H,G){H.minDepth=(G-1)*E;
H.maxDepth=H.depth=G*E
});
pv.Layout.Hierarchy.NodeLink.buildImplied.call(this,C)
};
pv.Layout.Partition.Fill=function(){pv.Layout.Partition.call(this);
pv.Layout.Hierarchy.Fill.constructor.call(this)
};
pv.Layout.Partition.Fill.prototype=pv.extend(pv.Layout.Partition);
pv.Layout.Partition.Fill.prototype.buildImplied=function(A){if(pv.Layout.Partition.prototype.buildImplied.call(this,A)){return 
}pv.Layout.Hierarchy.Fill.buildImplied.call(this,A)
};
pv.Layout.Arc=function(){pv.Layout.Network.call(this);
var B,D,A,C=this.buildImplied;
this.buildImplied=function(E){C.call(this,E);
D=E.directed;
B=E.orient=="radial"?"linear":"polar";
A=E.orient=="right"||E.orient=="top"
};
this.link.data(function(G){var F=G.sourceNode,E=G.targetNode;
return A!=(D||(F.breadth<E.breadth))?[F,E]:[E,F]
}).interpolate(function(){return B
})
};
pv.Layout.Arc.prototype=pv.extend(pv.Layout.Network).property("orient",String).property("directed",Boolean);
pv.Layout.Arc.prototype.defaults=new pv.Layout.Arc().extend(pv.Layout.Network.prototype.defaults).orient("bottom");
pv.Layout.Arc.prototype.sort=function(A){this.$sort=A;
return this
};
pv.Layout.Arc.prototype.buildImplied=function(N){if(pv.Layout.Network.prototype.buildImplied.call(this,N)){return 
}var B=N.nodes,E=N.orient,G=this.$sort,I=pv.range(B.length),M=N.width,H=N.height,A=Math.min(M,H)/2;
if(G){I.sort(function(P,O){return G(B[P],B[O])
})
}function D(O){switch(E){case"top":return -Math.PI/2;
case"bottom":return Math.PI/2;
case"left":return Math.PI;
case"right":return 0;
case"radial":return(O-0.25)*2*Math.PI
}}function L(O){switch(E){case"top":case"bottom":return O*M;
case"left":return 0;
case"right":return M;
case"radial":return M/2+A*Math.cos(D(O))
}}function K(O){switch(E){case"top":return 0;
case"bottom":return H;
case"left":case"right":return O*H;
case"radial":return H/2+A*Math.sin(D(O))
}}for(var F=0;
F<B.length;
F++){var C=B[I[F]],J=C.breadth=(F+0.5)/B.length;
C.x=L(J);
C.y=K(J);
C.midAngle=D(J)
}};
pv.Layout.Horizon=function(){pv.Layout.call(this);
var D=this,E,H,B,G,F,A,C=this.buildImplied;
this.buildImplied=function(I){C.call(this,I);
E=I.bands;
H=I.mode;
B=Math.round((H=="color"?0.5:1)*I.height);
G=I.backgroundStyle;
F=pv.ramp(G,I.negativeStyle).domain(0,E);
A=pv.ramp(G,I.positiveStyle).domain(0,E)
};
var E=new pv.Panel().data(function(){return pv.range(E*2)
}).overflow("hidden").height(function(){return B
}).top(function(I){return H=="color"?(I&1)*B:0
}).fillStyle(function(I){return I?null:G
});
this.band=new pv.Mark().top(function(J,I){return H=="mirror"&&I&1?(I+1>>1)*B:null
}).bottom(function(J,I){return H=="mirror"?(I&1?null:(I+1>>1)*-B):((I&1||-1)*(I+1>>1)*B)
}).fillStyle(function(J,I){return(I&1?F:A)((I>>1)+1)
});
this.band.add=function(I){return D.add(pv.Panel).extend(E).add(I).extend(this)
}
};
pv.Layout.Horizon.prototype=pv.extend(pv.Layout).property("bands",Number).property("mode",String).property("backgroundStyle",pv.color).property("positiveStyle",pv.color).property("negativeStyle",pv.color);
pv.Layout.Horizon.prototype.defaults=new pv.Layout.Horizon().extend(pv.Layout.prototype.defaults).bands(2).mode("offset").backgroundStyle("white").positiveStyle("#1f77b4").negativeStyle("#d62728");
pv.Layout.Rollup=function(){pv.Layout.Network.call(this);
var D=this,B,A,C=D.buildImplied;
this.buildImplied=function(E){C.call(this,E);
B=E.$rollup.nodes;
A=E.$rollup.links
};
this.node.data(function(){return B
}).size(function(E){return E.nodes.length*20
});
this.link.interpolate("polar").eccentricity(0.8);
this.link.add=function(E){return D.add(pv.Panel).data(function(){return A
}).add(E).extend(this)
}
};
pv.Layout.Rollup.prototype=pv.extend(pv.Layout.Network).property("directed",Boolean);
pv.Layout.Rollup.prototype.x=function(A){this.$x=pv.functor(A);
return this
};
pv.Layout.Rollup.prototype.y=function(A){this.$y=pv.functor(A);
return this
};
pv.Layout.Rollup.prototype.buildImplied=function(M){if(pv.Layout.Network.prototype.buildImplied.call(this,M)){return 
}var Q=M.nodes,A=M.links,D=M.directed,P=Q.length,I=[],H=[],V=0,R={},B={};
function N(X){return I[X]+","+H[X]
}var F=pv.Mark.stack,O={parent:this};
F.unshift(null);
for(var T=0;
T<P;
T++){O.index=T;
F[0]=Q[T];
I[T]=this.$x.apply(O,F);
H[T]=this.$y.apply(O,F)
}F.shift();
for(var T=0;
T<Q.length;
T++){var K=N(T),J=R[K];
if(!J){J=R[K]=pv.extend(Q[T]);
J.index=V++;
J.x=I[T];
J.y=H[T];
J.nodes=[]
}J.nodes.push(Q[T])
}for(var T=0;
T<A.length;
T++){var S=A[T].sourceNode,W=A[T].targetNode,U=R[N(S.index)],C=R[N(W.index)],G=!D&&U.index>C.index,E=G?C.index+","+U.index:U.index+","+C.index,L=B[E];
if(!L){L=B[E]={sourceNode:U,targetNode:C,linkValue:0,links:[]}
}L.links.push(A[T]);
L.linkValue+=A[T].linkValue
}M.$rollup={nodes:pv.values(R),links:pv.values(B)}
};
pv.Layout.Matrix=function(){pv.Layout.Network.call(this);
var D=this,G,B,A,F,E,C=D.buildImplied;
this.buildImplied=function(H){C.call(this,H);
G=H.nodes.length;
B=H.width/G;
A=H.height/G;
F=H.$matrix.labels;
E=H.$matrix.pairs
};
this.link.data(function(){return E
}).left(function(){return B*(this.index%G)
}).top(function(){return A*Math.floor(this.index/G)
}).width(function(){return B
}).height(function(){return A
}).lineWidth(1.5).strokeStyle("#fff").fillStyle(function(H){return H.linkValue?"#555":"#eee"
}).parent=this;
delete this.link.add;
this.label.data(function(){return F
}).left(function(){return this.index&1?B*((this.index>>1)+0.5):0
}).top(function(){return this.index&1?0:A*((this.index>>1)+0.5)
}).textMargin(4).textAlign(function(){return this.index&1?"left":"right"
}).textAngle(function(){return this.index&1?-Math.PI/2:0
});
delete this.node
};
pv.Layout.Matrix.prototype=pv.extend(pv.Layout.Network).property("directed",Boolean);
pv.Layout.Matrix.prototype.sort=function(A){this.$sort=A;
return this
};
pv.Layout.Matrix.prototype.buildImplied=function(C){if(pv.Layout.Network.prototype.buildImplied.call(this,C)){return 
}var F=C.nodes,A=C.links,M=this.$sort,E=F.length,B=pv.range(E),L=[],R=[],P={};
C.$matrix={labels:L,pairs:R};
if(M){B.sort(function(T,S){return M(F[T],F[S])
})
}for(var K=0;
K<E;
K++){for(var J=0;
J<E;
J++){var O=B[K],N=B[J],D={row:K,col:J,sourceNode:F[O],targetNode:F[N],linkValue:0};
R.push(P[O+"."+N]=D)
}}for(var K=0;
K<E;
K++){var O=B[K];
L.push(F[O],F[O])
}for(var K=0;
K<A.length;
K++){var I=A[K],G=I.sourceNode.index,Q=I.targetNode.index,H=I.linkValue;
P[G+"."+Q].linkValue+=H;
if(!C.directed){P[Q+"."+G].linkValue+=H
}}};
pv.Layout.Bullet=function(){pv.Layout.call(this);
var F=this,E=F.buildImplied,G=F.x=pv.Scale.linear(),C,B,H,D,A;
this.buildImplied=function(I){E.call(this,A=I);
C=I.orient;
B=/^left|right$/.test(C);
H=pv.ramp("#bbb","#eee").domain(0,Math.max(1,A.ranges.length-1));
D=pv.ramp("steelblue","lightsteelblue").domain(0,Math.max(1,A.measures.length-1))
};
(this.range=new pv.Mark()).data(function(){return A.ranges
}).reverse(true).left(function(){return C=="left"?0:null
}).top(function(){return C=="top"?0:null
}).right(function(){return C=="right"?0:null
}).bottom(function(){return C=="bottom"?0:null
}).width(function(I){return B?G(I):null
}).height(function(I){return B?null:G(I)
}).fillStyle(function(){return H(this.index)
}).antialias(false).parent=F;
(this.measure=new pv.Mark()).extend(this.range).data(function(){return A.measures
}).left(function(){return C=="left"?0:B?null:this.parent.width()/3.25
}).top(function(){return C=="top"?0:B?this.parent.height()/3.25:null
}).right(function(){return C=="right"?0:B?null:this.parent.width()/3.25
}).bottom(function(){return C=="bottom"?0:B?this.parent.height()/3.25:null
}).fillStyle(function(){return D(this.index)
}).parent=F;
(this.marker=new pv.Mark()).data(function(){return A.markers
}).left(function(I){return C=="left"?G(I):B?null:this.parent.width()/2
}).top(function(I){return C=="top"?G(I):B?this.parent.height()/2:null
}).right(function(I){return C=="right"?G(I):null
}).bottom(function(I){return C=="bottom"?G(I):null
}).strokeStyle("black").shape("bar").angle(function(){return B?0:Math.PI/2
}).parent=F;
(this.tick=new pv.Mark()).data(function(){return G.ticks(7)
}).left(function(I){return C=="left"?G(I):null
}).top(function(I){return C=="top"?G(I):null
}).right(function(I){return C=="right"?G(I):B?null:-6
}).bottom(function(I){return C=="bottom"?G(I):B?-8:null
}).height(function(){return B?6:null
}).width(function(){return B?null:6
}).parent=F
};
pv.Layout.Bullet.prototype=pv.extend(pv.Layout).property("orient",String).property("ranges").property("markers").property("measures").property("maximum",Number);
pv.Layout.Bullet.prototype.defaults=new pv.Layout.Bullet().extend(pv.Layout.prototype.defaults).orient("left").ranges([]).markers([]).measures([]);
pv.Layout.Bullet.prototype.buildImplied=function(B){pv.Layout.prototype.buildImplied.call(this,B);
var A=this.parent[/^left|right$/.test(B.orient)?"width":"height"]();
B.maximum=B.maximum||pv.max([].concat(B.ranges,B.markers,B.measures));
this.x.domain(0,B.maximum).range(0,A)
};
pv.Behavior={};
pv.Behavior.drag=function(){var F,C,E,H,A;
function B(J){C=this.index;
F=this.scene;
var I=this.mouse();
H=((E=J).fix=pv.vector(J.x,J.y)).minus(I);
A={x:this.parent.width()-(J.dx||0),y:this.parent.height()-(J.dy||0)};
F.mark.context(F,C,function(){this.render()
});
pv.Mark.dispatch("dragstart",F,C)
}function D(){if(!F){return 
}F.mark.context(F,C,function(){var I=this.mouse();
E.x=E.fix.x=Math.max(0,Math.min(H.x+I.x,A.x));
E.y=E.fix.y=Math.max(0,Math.min(H.y+I.y,A.y));
this.render()
});
pv.Mark.dispatch("drag",F,C)
}function G(){if(!F){return 
}E.fix=null;
F.mark.context(F,C,function(){this.render()
});
pv.Mark.dispatch("dragend",F,C);
F=null
}pv.listen(window,"mousemove",D);
pv.listen(window,"mouseup",G);
return B
};
pv.Behavior.point=function(A){var H,G=null,F=1,E=1,B=arguments.length?A*A:900;
function I(Q,R){var Y=Q[R],T={cost:Infinity};
for(var P=0,L=Y.visible&&Y.children.length;
P<L;
P++){var K=Y.children[P],N=K.mark,J;
if(N.type=="panel"){N.scene=K;
for(var O=0,M=K.length;
O<M;
O++){N.index=O;
J=I(K,O);
if(J.cost<T.cost){T=J
}}delete N.scene;
delete N.index
}else{if(N.$handlers.point){var V=N.mouse();
for(var O=0,M=K.length;
O<M;
O++){var S=K[O],X=V.x-S.left-(S.width||0)/2,W=V.y-S.top-(S.height||0)/2,U=F*X*X+E*W*W;
if(U<T.cost){T.distance=X*X+W*W;
T.cost=U;
T.scene=K;
T.index=O
}}}}}return T
}function D(){var J=I(this.scene,this.index);
if((J.cost==Infinity)||(J.distance>B)){J=null
}if(H){if(J&&(H.scene==J.scene)&&(H.index==J.index)){return 
}pv.Mark.dispatch("unpoint",H.scene,H.index)
}if(H=J){pv.Mark.dispatch("point",J.scene,J.index);
pv.listen(this.root.canvas(),"mouseout",C)
}}function C(J){if(H&&!pv.ancestor(this,J.relatedTarget)){pv.Mark.dispatch("unpoint",H.scene,H.index);
H=null
}}D.collapse=function(J){if(arguments.length){G=String(J);
switch(G){case"y":F=1;
E=0;
break;
case"x":F=0;
E=1;
break;
default:F=1;
E=1;
break
}return D
}return G
};
return D
};
pv.Behavior.select=function(){var F,C,E,B;
function A(H){C=this.index;
F=this.scene;
B=this.mouse();
E=H;
E.x=B.x;
E.y=B.y;
E.dx=E.dy=0;
pv.Mark.dispatch("selectstart",F,C)
}function D(){if(!F){return 
}F.mark.context(F,C,function(){var H=this.mouse();
E.x=Math.max(0,Math.min(B.x,H.x));
E.y=Math.max(0,Math.min(B.y,H.y));
E.dx=Math.min(this.width(),Math.max(H.x,B.x))-E.x;
E.dy=Math.min(this.height(),Math.max(H.y,B.y))-E.y;
this.render()
});
pv.Mark.dispatch("select",F,C)
}function G(){if(!F){return 
}pv.Mark.dispatch("selectend",F,C);
F=null
}pv.listen(window,"mousemove",D);
pv.listen(window,"mouseup",G);
return A
};
pv.Behavior.resize=function(D){var G,C,F,B;
function A(I){C=this.index;
G=this.scene;
B=this.mouse();
F=I;
switch(D){case"left":B.x=F.x+F.dx;
break;
case"right":B.x=F.x;
break;
case"top":B.y=F.y+F.dy;
break;
case"bottom":B.y=F.y;
break
}pv.Mark.dispatch("resizestart",G,C)
}function E(){if(!G){return 
}G.mark.context(G,C,function(){var I=this.mouse();
F.x=Math.max(0,Math.min(B.x,I.x));
F.y=Math.max(0,Math.min(B.y,I.y));
F.dx=Math.min(this.parent.width(),Math.max(I.x,B.x))-F.x;
F.dy=Math.min(this.parent.height(),Math.max(I.y,B.y))-F.y;
this.render()
});
pv.Mark.dispatch("resize",G,C)
}function H(){if(!G){return 
}pv.Mark.dispatch("resizeend",G,C);
G=null
}pv.listen(window,"mousemove",E);
pv.listen(window,"mouseup",H);
return A
};
pv.Behavior.pan=function(){var E,G,I,H,C,B;
function A(){G=this.index;
E=this.scene;
H=pv.vector(pv.event.pageX,pv.event.pageY);
I=this.transform();
C=1/(I.k*this.scale);
if(B){B={x:(1-I.k)*this.width(),y:(1-I.k)*this.height()}
}}function F(){if(!E){return 
}E.mark.context(E,G,function(){var K=(pv.event.pageX-H.x)*C,L=(pv.event.pageY-H.y)*C,J=I.translate(K,L);
if(B){J.x=Math.max(B.x,Math.min(0,J.x));
J.y=Math.max(B.y,Math.min(0,J.y))
}this.transform(J).render()
});
pv.Mark.dispatch("pan",E,G)
}function D(){E=null
}A.bound=function(J){if(arguments.length){B=Boolean(J);
return this
}return Boolean(B)
};
pv.listen(window,"mousemove",F);
pv.listen(window,"mouseup",D);
return A
};
pv.Behavior.zoom=function(C){var B;
if(!arguments.length){C=1/48
}function A(){var F=this.mouse(),E=pv.event.wheel*C,D=this.transform().translate(F.x,F.y).scale((E<0)?(1000/(1000-E)):((1000+E)/1000)).translate(-F.x,-F.y);
if(B){D.k=Math.max(1,D.k);
D.x=Math.max((1-D.k)*this.width(),Math.min(0,D.x));
D.y=Math.max((1-D.k)*this.height(),Math.min(0,D.y))
}this.transform(D).render();
pv.Mark.dispatch("zoom",this.scene,this.index)
}A.bound=function(D){if(arguments.length){B=Boolean(D);
return this
}return Boolean(B)
};
return A
};
pv.Geo=function(){};
pv.Geo.projections={mercator:{project:function(A){return{x:A.lng/180,y:A.lat>85?1:A.lat<-85?-1:Math.log(Math.tan(Math.PI/4+pv.radians(A.lat)/2))/Math.PI}
},invert:function(A){return{lng:A.x*180,lat:pv.degrees(2*Math.atan(Math.exp(A.y*Math.PI))-Math.PI/2)}
}},"gall-peters":{project:function(A){return{x:A.lng/180,y:Math.sin(pv.radians(A.lat))}
},invert:function(A){return{lng:A.x*180,lat:pv.degrees(Math.asin(A.y))}
}},sinusoidal:{project:function(A){return{x:pv.radians(A.lng)*Math.cos(pv.radians(A.lat))/Math.PI,y:A.lat/90}
},invert:function(A){return{lng:pv.degrees((A.x*Math.PI)/Math.cos(A.y*Math.PI/2)),lat:A.y*90}
}},aitoff:{project:function(D){var B=pv.radians(D.lng),C=pv.radians(D.lat),A=Math.acos(Math.cos(C)*Math.cos(B/2));
return{x:2*(A?(Math.cos(C)*Math.sin(B/2)*A/Math.sin(A)):0)/Math.PI,y:2*(A?(Math.sin(C)*A/Math.sin(A)):0)/Math.PI}
},invert:function(B){var A=B.x*Math.PI/2,C=B.y*Math.PI/2;
return{lng:pv.degrees(A/Math.cos(C)),lat:pv.degrees(C)}
}},hammer:{project:function(D){var A=pv.radians(D.lng),B=pv.radians(D.lat),C=Math.sqrt(1+Math.cos(B)*Math.cos(A/2));
return{x:2*Math.SQRT2*Math.cos(B)*Math.sin(A/2)/C/3,y:Math.SQRT2*Math.sin(B)/C/1.5}
},invert:function(B){var A=B.x*3,D=B.y*1.5,C=Math.sqrt(1-A*A/16-D*D/4);
return{lng:pv.degrees(2*Math.atan2(C*A,2*(2*C*C-1))),lat:pv.degrees(Math.asin(C*D))}
}},identity:{project:function(A){return{x:A.lng/180,y:A.lat/90}
},invert:function(A){return{lng:A.x*180,lat:A.y*90}
}}};
pv.Geo.scale=function(B){var F={x:0,y:0},M={x:1,y:1},E=[],D=pv.Geo.projections.identity,J=pv.Scale.linear(-1,1).range(0,1),H=pv.Scale.linear(-1,1).range(1,0),G={lng:0,lat:0},L,I;
function C(O){if(!L||(O.lng!=L.lng)||(O.lat!=L.lat)){L=O;
var N=K(O);
I={x:J(N.x),y:H(N.y)}
}return I
}function K(O){var N={lng:O.lng-G.lng,lat:O.lat};
return D.project(N)
}function A(N){var O=D.invert(N);
O.lng+=G.lng;
return O
}C.x=function(N){return C(N).x
};
C.y=function(N){return C(N).y
};
C.ticks={lng:function(N){var Q,O;
if(E.length>1){var P=pv.Scale.linear();
if(N==undefined){N=10
}Q=P.domain(E,function(R){return R.lat
}).ticks(N);
O=P.domain(E,function(R){return R.lng
}).ticks(N)
}else{Q=pv.range(-80,81,10);
O=pv.range(-180,181,10)
}return O.map(function(R){return Q.map(function(S){return{lat:S,lng:R}
})
})
},lat:function(N){return pv.transpose(C.ticks.lng(N))
}};
C.invert=function(N){return A({x:J.invert(N.x),y:H.invert(N.y)})
};
C.domain=function(R,N){if(arguments.length){E=(R instanceof Array)?((arguments.length>1)?pv.map(R,N):R):Array.prototype.slice.call(arguments);
if(E.length>1){var O=E.map(function(S){return S.lng
});
var P=E.map(function(S){return S.lat
});
G={lng:(pv.max(O)+pv.min(O))/2,lat:(pv.max(P)+pv.min(P))/2};
var Q=E.map(K);
J.domain(Q,function(S){return S.x
});
H.domain(Q,function(S){return S.y
})
}else{G={lng:0,lat:0};
J.domain(-1,1);
H.domain(-1,1)
}L=null;
return this
}return E
};
C.range=function(O,N){if(arguments.length){if(typeof O=="object"){F={x:Number(O.x),y:Number(O.y)};
M={x:Number(N.x),y:Number(N.y)}
}else{F={x:0,y:0};
M={x:Number(O),y:Number(N)}
}J.range(F.x,M.x);
H.range(M.y,F.y);
L=null;
return this
}return[F,M]
};
C.projection=function(N){if(arguments.length){D=typeof N=="string"?pv.Geo.projections[N]||pv.Geo.projections.identity:N;
return this.domain(E)
}return N
};
C.by=function(N){function O(){return C(N.apply(this,arguments))
}for(var P in C){O[P]=C[P]
}return O
};
if(arguments.length){C.projection(B)
}return C
};window.SonarWidgets={};
SonarWidgets.StackArea=function(A){this.wDivId=A;
this.wHeight;
this.wData;
this.wSnapshots;
this.wMetrics;
this.wColors;
this.height=function(B){this.wHeight=B;
return this
};
this.data=function(B){this.wData=B;
return this
};
this.snapshots=function(B){this.wSnapshots=B;
return this
};
this.metrics=function(B){this.wMetrics=B;
return this
};
this.colors=function(B){this.wColors=B;
return this
}
};
SonarWidgets.StackArea.prototype.render=function(){var B=this.wData;
var N=this.wMetrics;
var M=this.wSnapshots;
var H=this.wColors;
var F=$(this.wDivId);
var D="10.5px Arial,Helvetica,sans-serif";
var W=[];
for(i=0;
i<B[0].size();
i++){W[i]=0;
for(j=0;
j<N.size();
j++){W[i]+=B[j][i].y
}W[i]=""+Math.round(W[i]*10)/10
}var S=0;
for(i=0;
i<B[0].size();
i++){var T=0;
for(j=0;
j<B.size();
j++){T+=B[j][i].y
}if(T>S){S=T
}}var P=25;
var A=(Math.round(S)+"").length;
minMargin=A*7+Math.floor(A/3)*2;
if(minMargin>P){P=minMargin
}var O=40;
var J=F.getOffsetParent().getWidth()-P-40;
var Q=(this.wHeight==null?200:this.wHeight)+O;
var I=pv.Scale.linear(pv.blend(pv.map(B,function(X){return X
})),function(X){return X.x
}).range(0,J);
var G=pv.Scale.linear(0,S).range(0,Q-O);
var C=B[0].size();
var L=C-1;
function U(Y){var Z=I.invert(Y);
var X=pv.search(B[0].map(function(a){return a.x
}),Z);
X=X<0?(-X-2):X;
X=X<0?0:X;
return X
}var K=new pv.Panel().canvas(F).width(J).height(Q).left(P).right(20).bottom(30).top(20).strokeStyle("#CCC");
K.add(pv.Rule).data(I.ticks()).left(I).bottom(-10).height(10).anchor("bottom").add(pv.Label).text(I.tickFormat);
K.add(pv.Rule).data(G.ticks(6)).bottom(G).strokeStyle("rgba(128,128,128,.2)").anchor("left").add(pv.Label).text(G.tickFormat);
var R=K.add(pv.Layout.Stack).layers(B).x(function(X){return I(X.x)
}).y(function(X){return G(X.y)
}).layer.add(pv.Area).fillStyle(function(){return H[this.parent.index%H.size()][0]
}).strokeStyle("rgba(128,128,128,.8)");
var E=U(J/5);
var V=U(J*4/5);
K.add(pv.Panel).extend(R.parent).add(pv.Area).extend(R).fillStyle(null).strokeStyle(null).anchor(function(){return(L==C-1||L>V)?"right":((L==0||L<E)?"left":"center")
}).add(pv.Label).visible(function(X){return this.index==L&&X.y!=0
}).font(function(X){return Math.round(5+Math.sqrt(G(X.y)))+"px sans-serif"
}).textStyle("#DDD").text(function(X){return N[this.parent.index]+": "+X.y
});
K.add(pv.Label).left(8).top(16).font(D).text(function(){return"Total: "+W[L]
});
K.add(pv.Label).left(J/2).top(16).font(D).text(function(){return M[L].ld
});
eventColor="rgba(75,159,213,1)";
eventHoverColor="rgba(202,227,242,1)";
K.add(pv.Line).strokeStyle("rgba(0,0,0,.001)").data(M).left(function(X){return I(X.d)
}).bottom(0).anchor("top").add(pv.Dot).bottom(-6).shape("triangle").angle(pv.radians(180)).strokeStyle("grey").visible(function(X){return X.e.size()>0
}).fillStyle(function(){return this.index==L?eventHoverColor:eventColor
}).add(pv.Dot).radius(3).visible(function(X){return X.e.size()>0&&this.index==L
}).left(J/2+8).top(24).shape("triangle").fillStyle(function(){return this.index==L?eventHoverColor:eventColor
}).strokeStyle("grey").anchor("right").add(pv.Label).font(D).text(function(X){return X.e.size()==0?"":X.e[0]+(X.e[1]?" (... +"+(X.e.size()-1)+")":"")
});
K.add(pv.Bar).fillStyle("rgba(0,0,0,.001)").width(J+30).height(Q+30).event("mouseout",function(){i=-1;
return K
}).event("mousemove",function(){L=U(K.mouse().x);
return K
});
K.render()
};
SonarWidgets.Timeline=function(A){this.wDivId=A;
this.wHeight;
this.wData;
this.wSnapshots;
this.wMetrics;
this.wEvents;
this.height=function(B){this.wHeight=B;
return this
};
this.data=function(B){this.wData=B;
return this
};
this.snapshots=function(B){this.wSnapshots=B;
return this
};
this.metrics=function(B){this.wMetrics=B;
return this
};
this.events=function(B){this.wEvents=B;
return this
}
};
SonarWidgets.Timeline.prototype.render=function(){var B=this.wData;
var N=this.wMetrics;
var M=this.wSnapshots;
var A=this.wEvents;
var D=$(this.wDivId);
var C="10.5px Arial,Helvetica,sans-serif";
var O=4+Math.max(this.wMetrics.size(),A?2:1)*18;
var I=D.getOffsetParent().getWidth()-60;
var R=(this.wHeight==null||this.wHeight<=0?80:this.wHeight)+O;
var P=R-O;
var G=pv.Scale.linear(pv.blend(pv.map(B,function(S){return S
})),function(S){return S.x
}).range(0,I);
var F=new Array(B.size());
for(var Q=0;
Q<B.size();
Q++){F[Q]=pv.Scale.linear(B[Q],function(S){return S.y
}).range(20,P)
}var E="linear";
var K=B[0].size()-1;
var L=new pv.Panel().canvas(D).width(I).height(R).left(20).right(20).bottom(30).top(5).strokeStyle("#CCC");
L.add(pv.Rule).data(G.ticks()).left(G).bottom(-10).height(10).anchor("bottom").add(pv.Label).text(G.tickFormat);
var J=L.add(pv.Panel).data(B);
var H=J.add(pv.Line).data(function(S){return S
}).left(function(S){return G(S.x)
}).bottom(function(T){var S=F[this.parent.index](T.y);
return isNaN(S)?P:S
}).interpolate(function(){return E
}).lineWidth(2);
H.add(pv.Dot).data(function(S){return[S[K]]
}).fillStyle(function(){return H.strokeStyle()
}).strokeStyle("#000").size(20).lineWidth(1).add(pv.Dot).radius(3).left(10).top(function(){return 10+this.parent.index*14
}).anchor("right").add(pv.Label).font(C).text(function(S){return N[this.parent.index]+": "+S.yl
});
L.add(pv.Label).left(I/2).top(16).font(C).text(function(){return M[K].d
});
if(A){eventColor="rgba(75,159,213,1)";
eventHoverColor="rgba(202,227,242,1)";
L.add(pv.Line).strokeStyle("rgba(0,0,0,.001)").data(A).left(function(S){return G(S.d)
}).bottom(0).anchor("top").add(pv.Dot).bottom(-6).shape("triangle").angle(pv.radians(180)).strokeStyle("grey").fillStyle(function(S){return S.sid==M[K].sid?eventHoverColor:eventColor
}).add(pv.Dot).radius(3).visible(function(S){return S.sid==M[K].sid
}).left(I/2+8).top(24).shape("triangle").fillStyle(function(S){return S.sid==M[K].sid?eventHoverColor:eventColor
}).strokeStyle("grey").anchor("right").add(pv.Label).font(C).text(function(S){return S.l[0].n+(S.l[1]?" (... +"+(S.l.size()-1)+")":"")
})
}L.add(pv.Bar).fillStyle("rgba(0,0,0,.001)").width(I+30).height(R+30).event("mouseout",function(){Q=-1;
return L
}).event("mousemove",function(){var S=G.invert(L.mouse().x);
K=pv.search(B[0].map(function(T){return T.x
}),S);
K=K<0?(-K-2):K;
K=K<0?0:K;
return L
});
L.render()
};/*! jQuery v@1.8.1 jquery.com | jquery.org/license */
(function(a,b){function G(a){var b=F[a]={};return p.each(a.split(s),function(a,c){b[c]=!0}),b}function J(a,c,d){if(d===b&&a.nodeType===1){var e="data-"+c.replace(I,"-$1").toLowerCase();d=a.getAttribute(e);if(typeof d=="string"){try{d=d==="true"?!0:d==="false"?!1:d==="null"?null:+d+""===d?+d:H.test(d)?p.parseJSON(d):d}catch(f){}p.data(a,c,d)}else d=b}return d}function K(a){var b;for(b in a){if(b==="data"&&p.isEmptyObject(a[b]))continue;if(b!=="toJSON")return!1}return!0}function ba(){return!1}function bb(){return!0}function bh(a){return!a||!a.parentNode||a.parentNode.nodeType===11}function bi(a,b){do a=a[b];while(a&&a.nodeType!==1);return a}function bj(a,b,c){b=b||0;if(p.isFunction(b))return p.grep(a,function(a,d){var e=!!b.call(a,d,a);return e===c});if(b.nodeType)return p.grep(a,function(a,d){return a===b===c});if(typeof b=="string"){var d=p.grep(a,function(a){return a.nodeType===1});if(be.test(b))return p.filter(b,d,!c);b=p.filter(b,d)}return p.grep(a,function(a,d){return p.inArray(a,b)>=0===c})}function bk(a){var b=bl.split("|"),c=a.createDocumentFragment();if(c.createElement)while(b.length)c.createElement(b.pop());return c}function bC(a,b){return a.getElementsByTagName(b)[0]||a.appendChild(a.ownerDocument.createElement(b))}function bD(a,b){if(b.nodeType!==1||!p.hasData(a))return;var c,d,e,f=p._data(a),g=p._data(b,f),h=f.events;if(h){delete g.handle,g.events={};for(c in h)for(d=0,e=h[c].length;d<e;d++)p.event.add(b,c,h[c][d])}g.data&&(g.data=p.extend({},g.data))}function bE(a,b){var c;if(b.nodeType!==1)return;b.clearAttributes&&b.clearAttributes(),b.mergeAttributes&&b.mergeAttributes(a),c=b.nodeName.toLowerCase(),c==="object"?(b.parentNode&&(b.outerHTML=a.outerHTML),p.support.html5Clone&&a.innerHTML&&!p.trim(b.innerHTML)&&(b.innerHTML=a.innerHTML)):c==="input"&&bv.test(a.type)?(b.defaultChecked=b.checked=a.checked,b.value!==a.value&&(b.value=a.value)):c==="option"?b.selected=a.defaultSelected:c==="input"||c==="textarea"?b.defaultValue=a.defaultValue:c==="script"&&b.text!==a.text&&(b.text=a.text),b.removeAttribute(p.expando)}function bF(a){return typeof a.getElementsByTagName!="undefined"?a.getElementsByTagName("*"):typeof a.querySelectorAll!="undefined"?a.querySelectorAll("*"):[]}function bG(a){bv.test(a.type)&&(a.defaultChecked=a.checked)}function bY(a,b){if(b in a)return b;var c=b.charAt(0).toUpperCase()+b.slice(1),d=b,e=bW.length;while(e--){b=bW[e]+c;if(b in a)return b}return d}function bZ(a,b){return a=b||a,p.css(a,"display")==="none"||!p.contains(a.ownerDocument,a)}function b$(a,b){var c,d,e=[],f=0,g=a.length;for(;f<g;f++){c=a[f];if(!c.style)continue;e[f]=p._data(c,"olddisplay"),b?(!e[f]&&c.style.display==="none"&&(c.style.display=""),c.style.display===""&&bZ(c)&&(e[f]=p._data(c,"olddisplay",cc(c.nodeName)))):(d=bH(c,"display"),!e[f]&&d!=="none"&&p._data(c,"olddisplay",d))}for(f=0;f<g;f++){c=a[f];if(!c.style)continue;if(!b||c.style.display==="none"||c.style.display==="")c.style.display=b?e[f]||"":"none"}return a}function b_(a,b,c){var d=bP.exec(b);return d?Math.max(0,d[1]-(c||0))+(d[2]||"px"):b}function ca(a,b,c,d){var e=c===(d?"border":"content")?4:b==="width"?1:0,f=0;for(;e<4;e+=2)c==="margin"&&(f+=p.css(a,c+bV[e],!0)),d?(c==="content"&&(f-=parseFloat(bH(a,"padding"+bV[e]))||0),c!=="margin"&&(f-=parseFloat(bH(a,"border"+bV[e]+"Width"))||0)):(f+=parseFloat(bH(a,"padding"+bV[e]))||0,c!=="padding"&&(f+=parseFloat(bH(a,"border"+bV[e]+"Width"))||0));return f}function cb(a,b,c){var d=b==="width"?a.offsetWidth:a.offsetHeight,e=!0,f=p.support.boxSizing&&p.css(a,"boxSizing")==="border-box";if(d<=0||d==null){d=bH(a,b);if(d<0||d==null)d=a.style[b];if(bQ.test(d))return d;e=f&&(p.support.boxSizingReliable||d===a.style[b]),d=parseFloat(d)||0}return d+ca(a,b,c||(f?"border":"content"),e)+"px"}function cc(a){if(bS[a])return bS[a];var b=p("<"+a+">").appendTo(e.body),c=b.css("display");b.remove();if(c==="none"||c===""){bI=e.body.appendChild(bI||p.extend(e.createElement("iframe"),{frameBorder:0,width:0,height:0}));if(!bJ||!bI.createElement)bJ=(bI.contentWindow||bI.contentDocument).document,bJ.write("<!doctype html><html><body>"),bJ.close();b=bJ.body.appendChild(bJ.createElement(a)),c=bH(b,"display"),e.body.removeChild(bI)}return bS[a]=c,c}function ci(a,b,c,d){var e;if(p.isArray(b))p.each(b,function(b,e){c||ce.test(a)?d(a,e):ci(a+"["+(typeof e=="object"?b:"")+"]",e,c,d)});else if(!c&&p.type(b)==="object")for(e in b)ci(a+"["+e+"]",b[e],c,d);else d(a,b)}function cz(a){return function(b,c){typeof b!="string"&&(c=b,b="*");var d,e,f,g=b.toLowerCase().split(s),h=0,i=g.length;if(p.isFunction(c))for(;h<i;h++)d=g[h],f=/^\+/.test(d),f&&(d=d.substr(1)||"*"),e=a[d]=a[d]||[],e[f?"unshift":"push"](c)}}function cA(a,c,d,e,f,g){f=f||c.dataTypes[0],g=g||{},g[f]=!0;var h,i=a[f],j=0,k=i?i.length:0,l=a===cv;for(;j<k&&(l||!h);j++)h=i[j](c,d,e),typeof h=="string"&&(!l||g[h]?h=b:(c.dataTypes.unshift(h),h=cA(a,c,d,e,h,g)));return(l||!h)&&!g["*"]&&(h=cA(a,c,d,e,"*",g)),h}function cB(a,c){var d,e,f=p.ajaxSettings.flatOptions||{};for(d in c)c[d]!==b&&((f[d]?a:e||(e={}))[d]=c[d]);e&&p.extend(!0,a,e)}function cC(a,c,d){var e,f,g,h,i=a.contents,j=a.dataTypes,k=a.responseFields;for(f in k)f in d&&(c[k[f]]=d[f]);while(j[0]==="*")j.shift(),e===b&&(e=a.mimeType||c.getResponseHeader("content-type"));if(e)for(f in i)if(i[f]&&i[f].test(e)){j.unshift(f);break}if(j[0]in d)g=j[0];else{for(f in d){if(!j[0]||a.converters[f+" "+j[0]]){g=f;break}h||(h=f)}g=g||h}if(g)return g!==j[0]&&j.unshift(g),d[g]}function cD(a,b){var c,d,e,f,g=a.dataTypes.slice(),h=g[0],i={},j=0;a.dataFilter&&(b=a.dataFilter(b,a.dataType));if(g[1])for(c in a.converters)i[c.toLowerCase()]=a.converters[c];for(;e=g[++j];)if(e!=="*"){if(h!=="*"&&h!==e){c=i[h+" "+e]||i["* "+e];if(!c)for(d in i){f=d.split(" ");if(f[1]===e){c=i[h+" "+f[0]]||i["* "+f[0]];if(c){c===!0?c=i[d]:i[d]!==!0&&(e=f[0],g.splice(j--,0,e));break}}}if(c!==!0)if(c&&a["throws"])b=c(b);else try{b=c(b)}catch(k){return{state:"parsererror",error:c?k:"No conversion from "+h+" to "+e}}}h=e}return{state:"success",data:b}}function cL(){try{return new a.XMLHttpRequest}catch(b){}}function cM(){try{return new a.ActiveXObject("Microsoft.XMLHTTP")}catch(b){}}function cU(){return setTimeout(function(){cN=b},0),cN=p.now()}function cV(a,b){p.each(b,function(b,c){var d=(cT[b]||[]).concat(cT["*"]),e=0,f=d.length;for(;e<f;e++)if(d[e].call(a,b,c))return})}function cW(a,b,c){var d,e=0,f=0,g=cS.length,h=p.Deferred().always(function(){delete i.elem}),i=function(){var b=cN||cU(),c=Math.max(0,j.startTime+j.duration-b),d=1-(c/j.duration||0),e=0,f=j.tweens.length;for(;e<f;e++)j.tweens[e].run(d);return h.notifyWith(a,[j,d,c]),d<1&&f?c:(h.resolveWith(a,[j]),!1)},j=h.promise({elem:a,props:p.extend({},b),opts:p.extend(!0,{specialEasing:{}},c),originalProperties:b,originalOptions:c,startTime:cN||cU(),duration:c.duration,tweens:[],createTween:function(b,c,d){var e=p.Tween(a,j.opts,b,c,j.opts.specialEasing[b]||j.opts.easing);return j.tweens.push(e),e},stop:function(b){var c=0,d=b?j.tweens.length:0;for(;c<d;c++)j.tweens[c].run(1);return b?h.resolveWith(a,[j,b]):h.rejectWith(a,[j,b]),this}}),k=j.props;cX(k,j.opts.specialEasing);for(;e<g;e++){d=cS[e].call(j,a,k,j.opts);if(d)return d}return cV(j,k),p.isFunction(j.opts.start)&&j.opts.start.call(a,j),p.fx.timer(p.extend(i,{anim:j,queue:j.opts.queue,elem:a})),j.progress(j.opts.progress).done(j.opts.done,j.opts.complete).fail(j.opts.fail).always(j.opts.always)}function cX(a,b){var c,d,e,f,g;for(c in a){d=p.camelCase(c),e=b[d],f=a[c],p.isArray(f)&&(e=f[1],f=a[c]=f[0]),c!==d&&(a[d]=f,delete a[c]),g=p.cssHooks[d];if(g&&"expand"in g){f=g.expand(f),delete a[d];for(c in f)c in a||(a[c]=f[c],b[c]=e)}else b[d]=e}}function cY(a,b,c){var d,e,f,g,h,i,j,k,l=this,m=a.style,n={},o=[],q=a.nodeType&&bZ(a);c.queue||(j=p._queueHooks(a,"fx"),j.unqueued==null&&(j.unqueued=0,k=j.empty.fire,j.empty.fire=function(){j.unqueued||k()}),j.unqueued++,l.always(function(){l.always(function(){j.unqueued--,p.queue(a,"fx").length||j.empty.fire()})})),a.nodeType===1&&("height"in b||"width"in b)&&(c.overflow=[m.overflow,m.overflowX,m.overflowY],p.css(a,"display")==="inline"&&p.css(a,"float")==="none"&&(!p.support.inlineBlockNeedsLayout||cc(a.nodeName)==="inline"?m.display="inline-block":m.zoom=1)),c.overflow&&(m.overflow="hidden",p.support.shrinkWrapBlocks||l.done(function(){m.overflow=c.overflow[0],m.overflowX=c.overflow[1],m.overflowY=c.overflow[2]}));for(d in b){f=b[d];if(cP.exec(f)){delete b[d];if(f===(q?"hide":"show"))continue;o.push(d)}}g=o.length;if(g){h=p._data(a,"fxshow")||p._data(a,"fxshow",{}),q?p(a).show():l.done(function(){p(a).hide()}),l.done(function(){var b;p.removeData(a,"fxshow",!0);for(b in n)p.style(a,b,n[b])});for(d=0;d<g;d++)e=o[d],i=l.createTween(e,q?h[e]:0),n[e]=h[e]||p.style(a,e),e in h||(h[e]=i.start,q&&(i.end=i.start,i.start=e==="width"||e==="height"?1:0))}}function cZ(a,b,c,d,e){return new cZ.prototype.init(a,b,c,d,e)}function c$(a,b){var c,d={height:a},e=0;b=b?1:0;for(;e<4;e+=2-b)c=bV[e],d["margin"+c]=d["padding"+c]=a;return b&&(d.opacity=d.width=a),d}function da(a){return p.isWindow(a)?a:a.nodeType===9?a.defaultView||a.parentWindow:!1}var c,d,e=a.document,f=a.location,g=a.navigator,h=a.jQuery,i=a.$,j=Array.prototype.push,k=Array.prototype.slice,l=Array.prototype.indexOf,m=Object.prototype.toString,n=Object.prototype.hasOwnProperty,o=String.prototype.trim,p=function(a,b){return new p.fn.init(a,b,c)},q=/[\-+]?(?:\d*\.|)\d+(?:[eE][\-+]?\d+|)/.source,r=/\S/,s=/\s+/,t=/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,u=/^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/,v=/^<(\w+)\s*\/?>(?:<\/\1>|)$/,w=/^[\],:{}\s]*$/,x=/(?:^|:|,)(?:\s*\[)+/g,y=/\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g,z=/"[^"\\\r\n]*"|true|false|null|-?(?:\d\d*\.|)\d+(?:[eE][\-+]?\d+|)/g,A=/^-ms-/,B=/-([\da-z])/gi,C=function(a,b){return(b+"").toUpperCase()},D=function(){e.addEventListener?(e.removeEventListener("DOMContentLoaded",D,!1),p.ready()):e.readyState==="complete"&&(e.detachEvent("onreadystatechange",D),p.ready())},E={};p.fn=p.prototype={constructor:p,init:function(a,c,d){var f,g,h,i;if(!a)return this;if(a.nodeType)return this.context=this[0]=a,this.length=1,this;if(typeof a=="string"){a.charAt(0)==="<"&&a.charAt(a.length-1)===">"&&a.length>=3?f=[null,a,null]:f=u.exec(a);if(f&&(f[1]||!c)){if(f[1])return c=c instanceof p?c[0]:c,i=c&&c.nodeType?c.ownerDocument||c:e,a=p.parseHTML(f[1],i,!0),v.test(f[1])&&p.isPlainObject(c)&&this.attr.call(a,c,!0),p.merge(this,a);g=e.getElementById(f[2]);if(g&&g.parentNode){if(g.id!==f[2])return d.find(a);this.length=1,this[0]=g}return this.context=e,this.selector=a,this}return!c||c.jquery?(c||d).find(a):this.constructor(c).find(a)}return p.isFunction(a)?d.ready(a):(a.selector!==b&&(this.selector=a.selector,this.context=a.context),p.makeArray(a,this))},selector:"",jquery:"1.8.1",length:0,size:function(){return this.length},toArray:function(){return k.call(this)},get:function(a){return a==null?this.toArray():a<0?this[this.length+a]:this[a]},pushStack:function(a,b,c){var d=p.merge(this.constructor(),a);return d.prevObject=this,d.context=this.context,b==="find"?d.selector=this.selector+(this.selector?" ":"")+c:b&&(d.selector=this.selector+"."+b+"("+c+")"),d},each:function(a,b){return p.each(this,a,b)},ready:function(a){return p.ready.promise().done(a),this},eq:function(a){return a=+a,a===-1?this.slice(a):this.slice(a,a+1)},first:function(){return this.eq(0)},last:function(){return this.eq(-1)},slice:function(){return this.pushStack(k.apply(this,arguments),"slice",k.call(arguments).join(","))},map:function(a){return this.pushStack(p.map(this,function(b,c){return a.call(b,c,b)}))},end:function(){return this.prevObject||this.constructor(null)},push:j,sort:[].sort,splice:[].splice},p.fn.init.prototype=p.fn,p.extend=p.fn.extend=function(){var a,c,d,e,f,g,h=arguments[0]||{},i=1,j=arguments.length,k=!1;typeof h=="boolean"&&(k=h,h=arguments[1]||{},i=2),typeof h!="object"&&!p.isFunction(h)&&(h={}),j===i&&(h=this,--i);for(;i<j;i++)if((a=arguments[i])!=null)for(c in a){d=h[c],e=a[c];if(h===e)continue;k&&e&&(p.isPlainObject(e)||(f=p.isArray(e)))?(f?(f=!1,g=d&&p.isArray(d)?d:[]):g=d&&p.isPlainObject(d)?d:{},h[c]=p.extend(k,g,e)):e!==b&&(h[c]=e)}return h},p.extend({noConflict:function(b){return a.$===p&&(a.$=i),b&&a.jQuery===p&&(a.jQuery=h),p},isReady:!1,readyWait:1,holdReady:function(a){a?p.readyWait++:p.ready(!0)},ready:function(a){if(a===!0?--p.readyWait:p.isReady)return;if(!e.body)return setTimeout(p.ready,1);p.isReady=!0;if(a!==!0&&--p.readyWait>0)return;d.resolveWith(e,[p]),p.fn.trigger&&p(e).trigger("ready").off("ready")},isFunction:function(a){return p.type(a)==="function"},isArray:Array.isArray||function(a){return p.type(a)==="array"},isWindow:function(a){return a!=null&&a==a.window},isNumeric:function(a){return!isNaN(parseFloat(a))&&isFinite(a)},type:function(a){return a==null?String(a):E[m.call(a)]||"object"},isPlainObject:function(a){if(!a||p.type(a)!=="object"||a.nodeType||p.isWindow(a))return!1;try{if(a.constructor&&!n.call(a,"constructor")&&!n.call(a.constructor.prototype,"isPrototypeOf"))return!1}catch(c){return!1}var d;for(d in a);return d===b||n.call(a,d)},isEmptyObject:function(a){var b;for(b in a)return!1;return!0},error:function(a){throw new Error(a)},parseHTML:function(a,b,c){var d;return!a||typeof a!="string"?null:(typeof b=="boolean"&&(c=b,b=0),b=b||e,(d=v.exec(a))?[b.createElement(d[1])]:(d=p.buildFragment([a],b,c?null:[]),p.merge([],(d.cacheable?p.clone(d.fragment):d.fragment).childNodes)))},parseJSON:function(b){if(!b||typeof b!="string")return null;b=p.trim(b);if(a.JSON&&a.JSON.parse)return a.JSON.parse(b);if(w.test(b.replace(y,"@").replace(z,"]").replace(x,"")))return(new Function("return "+b))();p.error("Invalid JSON: "+b)},parseXML:function(c){var d,e;if(!c||typeof c!="string")return null;try{a.DOMParser?(e=new DOMParser,d=e.parseFromString(c,"text/xml")):(d=new ActiveXObject("Microsoft.XMLDOM"),d.async="false",d.loadXML(c))}catch(f){d=b}return(!d||!d.documentElement||d.getElementsByTagName("parsererror").length)&&p.error("Invalid XML: "+c),d},noop:function(){},globalEval:function(b){b&&r.test(b)&&(a.execScript||function(b){a.eval.call(a,b)})(b)},camelCase:function(a){return a.replace(A,"ms-").replace(B,C)},nodeName:function(a,b){return a.nodeName&&a.nodeName.toUpperCase()===b.toUpperCase()},each:function(a,c,d){var e,f=0,g=a.length,h=g===b||p.isFunction(a);if(d){if(h){for(e in a)if(c.apply(a[e],d)===!1)break}else for(;f<g;)if(c.apply(a[f++],d)===!1)break}else if(h){for(e in a)if(c.call(a[e],e,a[e])===!1)break}else for(;f<g;)if(c.call(a[f],f,a[f++])===!1)break;return a},trim:o&&!o.call("﻿ ")?function(a){return a==null?"":o.call(a)}:function(a){return a==null?"":a.toString().replace(t,"")},makeArray:function(a,b){var c,d=b||[];return a!=null&&(c=p.type(a),a.length==null||c==="string"||c==="function"||c==="regexp"||p.isWindow(a)?j.call(d,a):p.merge(d,a)),d},inArray:function(a,b,c){var d;if(b){if(l)return l.call(b,a,c);d=b.length,c=c?c<0?Math.max(0,d+c):c:0;for(;c<d;c++)if(c in b&&b[c]===a)return c}return-1},merge:function(a,c){var d=c.length,e=a.length,f=0;if(typeof d=="number")for(;f<d;f++)a[e++]=c[f];else while(c[f]!==b)a[e++]=c[f++];return a.length=e,a},grep:function(a,b,c){var d,e=[],f=0,g=a.length;c=!!c;for(;f<g;f++)d=!!b(a[f],f),c!==d&&e.push(a[f]);return e},map:function(a,c,d){var e,f,g=[],h=0,i=a.length,j=a instanceof p||i!==b&&typeof i=="number"&&(i>0&&a[0]&&a[i-1]||i===0||p.isArray(a));if(j)for(;h<i;h++)e=c(a[h],h,d),e!=null&&(g[g.length]=e);else for(f in a)e=c(a[f],f,d),e!=null&&(g[g.length]=e);return g.concat.apply([],g)},guid:1,proxy:function(a,c){var d,e,f;return typeof c=="string"&&(d=a[c],c=a,a=d),p.isFunction(a)?(e=k.call(arguments,2),f=function(){return a.apply(c,e.concat(k.call(arguments)))},f.guid=a.guid=a.guid||f.guid||p.guid++,f):b},access:function(a,c,d,e,f,g,h){var i,j=d==null,k=0,l=a.length;if(d&&typeof d=="object"){for(k in d)p.access(a,c,k,d[k],1,g,e);f=1}else if(e!==b){i=h===b&&p.isFunction(e),j&&(i?(i=c,c=function(a,b,c){return i.call(p(a),c)}):(c.call(a,e),c=null));if(c)for(;k<l;k++)c(a[k],d,i?e.call(a[k],k,c(a[k],d)):e,h);f=1}return f?a:j?c.call(a):l?c(a[0],d):g},now:function(){return(new Date).getTime()}}),p.ready.promise=function(b){if(!d){d=p.Deferred();if(e.readyState==="complete")setTimeout(p.ready,1);else if(e.addEventListener)e.addEventListener("DOMContentLoaded",D,!1),a.addEventListener("load",p.ready,!1);else{e.attachEvent("onreadystatechange",D),a.attachEvent("onload",p.ready);var c=!1;try{c=a.frameElement==null&&e.documentElement}catch(f){}c&&c.doScroll&&function g(){if(!p.isReady){try{c.doScroll("left")}catch(a){return setTimeout(g,50)}p.ready()}}()}}return d.promise(b)},p.each("Boolean Number String Function Array Date RegExp Object".split(" "),function(a,b){E["[object "+b+"]"]=b.toLowerCase()}),c=p(e);var F={};p.Callbacks=function(a){a=typeof a=="string"?F[a]||G(a):p.extend({},a);var c,d,e,f,g,h,i=[],j=!a.once&&[],k=function(b){c=a.memory&&b,d=!0,h=f||0,f=0,g=i.length,e=!0;for(;i&&h<g;h++)if(i[h].apply(b[0],b[1])===!1&&a.stopOnFalse){c=!1;break}e=!1,i&&(j?j.length&&k(j.shift()):c?i=[]:l.disable())},l={add:function(){if(i){var b=i.length;(function d(b){p.each(b,function(b,c){var e=p.type(c);e==="function"&&(!a.unique||!l.has(c))?i.push(c):c&&c.length&&e!=="string"&&d(c)})})(arguments),e?g=i.length:c&&(f=b,k(c))}return this},remove:function(){return i&&p.each(arguments,function(a,b){var c;while((c=p.inArray(b,i,c))>-1)i.splice(c,1),e&&(c<=g&&g--,c<=h&&h--)}),this},has:function(a){return p.inArray(a,i)>-1},empty:function(){return i=[],this},disable:function(){return i=j=c=b,this},disabled:function(){return!i},lock:function(){return j=b,c||l.disable(),this},locked:function(){return!j},fireWith:function(a,b){return b=b||[],b=[a,b.slice?b.slice():b],i&&(!d||j)&&(e?j.push(b):k(b)),this},fire:function(){return l.fireWith(this,arguments),this},fired:function(){return!!d}};return l},p.extend({Deferred:function(a){var b=[["resolve","done",p.Callbacks("once memory"),"resolved"],["reject","fail",p.Callbacks("once memory"),"rejected"],["notify","progress",p.Callbacks("memory")]],c="pending",d={state:function(){return c},always:function(){return e.done(arguments).fail(arguments),this},then:function(){var a=arguments;return p.Deferred(function(c){p.each(b,function(b,d){var f=d[0],g=a[b];e[d[1]](p.isFunction(g)?function(){var a=g.apply(this,arguments);a&&p.isFunction(a.promise)?a.promise().done(c.resolve).fail(c.reject).progress(c.notify):c[f+"With"](this===e?c:this,[a])}:c[f])}),a=null}).promise()},promise:function(a){return typeof a=="object"?p.extend(a,d):d}},e={};return d.pipe=d.then,p.each(b,function(a,f){var g=f[2],h=f[3];d[f[1]]=g.add,h&&g.add(function(){c=h},b[a^1][2].disable,b[2][2].lock),e[f[0]]=g.fire,e[f[0]+"With"]=g.fireWith}),d.promise(e),a&&a.call(e,e),e},when:function(a){var b=0,c=k.call(arguments),d=c.length,e=d!==1||a&&p.isFunction(a.promise)?d:0,f=e===1?a:p.Deferred(),g=function(a,b,c){return function(d){b[a]=this,c[a]=arguments.length>1?k.call(arguments):d,c===h?f.notifyWith(b,c):--e||f.resolveWith(b,c)}},h,i,j;if(d>1){h=new Array(d),i=new Array(d),j=new Array(d);for(;b<d;b++)c[b]&&p.isFunction(c[b].promise)?c[b].promise().done(g(b,j,c)).fail(f.reject).progress(g(b,i,h)):--e}return e||f.resolveWith(j,c),f.promise()}}),p.support=function(){var b,c,d,f,g,h,i,j,k,l,m,n=e.createElement("div");n.setAttribute("className","t"),n.innerHTML="  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>",c=n.getElementsByTagName("*"),d=n.getElementsByTagName("a")[0],d.style.cssText="top:1px;float:left;opacity:.5";if(!c||!c.length||!d)return{};f=e.createElement("select"),g=f.appendChild(e.createElement("option")),h=n.getElementsByTagName("input")[0],b={leadingWhitespace:n.firstChild.nodeType===3,tbody:!n.getElementsByTagName("tbody").length,htmlSerialize:!!n.getElementsByTagName("link").length,style:/top/.test(d.getAttribute("style")),hrefNormalized:d.getAttribute("href")==="/a",opacity:/^0.5/.test(d.style.opacity),cssFloat:!!d.style.cssFloat,checkOn:h.value==="on",optSelected:g.selected,getSetAttribute:n.className!=="t",enctype:!!e.createElement("form").enctype,html5Clone:e.createElement("nav").cloneNode(!0).outerHTML!=="<:nav></:nav>",boxModel:e.compatMode==="CSS1Compat",submitBubbles:!0,changeBubbles:!0,focusinBubbles:!1,deleteExpando:!0,noCloneEvent:!0,inlineBlockNeedsLayout:!1,shrinkWrapBlocks:!1,reliableMarginRight:!0,boxSizingReliable:!0,pixelPosition:!1},h.checked=!0,b.noCloneChecked=h.cloneNode(!0).checked,f.disabled=!0,b.optDisabled=!g.disabled;try{delete n.test}catch(o){b.deleteExpando=!1}!n.addEventListener&&n.attachEvent&&n.fireEvent&&(n.attachEvent("onclick",m=function(){b.noCloneEvent=!1}),n.cloneNode(!0).fireEvent("onclick"),n.detachEvent("onclick",m)),h=e.createElement("input"),h.value="t",h.setAttribute("type","radio"),b.radioValue=h.value==="t",h.setAttribute("checked","checked"),h.setAttribute("name","t"),n.appendChild(h),i=e.createDocumentFragment(),i.appendChild(n.lastChild),b.checkClone=i.cloneNode(!0).cloneNode(!0).lastChild.checked,b.appendChecked=h.checked,i.removeChild(h),i.appendChild(n);if(n.attachEvent)for(k in{submit:!0,change:!0,focusin:!0})j="on"+k,l=j in n,l||(n.setAttribute(j,"return;"),l=typeof n[j]=="function"),b[k+"Bubbles"]=l;return p(function(){var c,d,f,g,h="padding:0;margin:0;border:0;display:block;overflow:hidden;",i=e.getElementsByTagName("body")[0];if(!i)return;c=e.createElement("div"),c.style.cssText="visibility:hidden;border:0;width:0;height:0;position:static;top:0;margin-top:1px",i.insertBefore(c,i.firstChild),d=e.createElement("div"),c.appendChild(d),d.innerHTML="<table><tr><td></td><td>t</td></tr></table>",f=d.getElementsByTagName("td"),f[0].style.cssText="padding:0;margin:0;border:0;display:none",l=f[0].offsetHeight===0,f[0].style.display="",f[1].style.display="none",b.reliableHiddenOffsets=l&&f[0].offsetHeight===0,d.innerHTML="",d.style.cssText="box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;padding:1px;border:1px;display:block;width:4px;margin-top:1%;position:absolute;top:1%;",b.boxSizing=d.offsetWidth===4,b.doesNotIncludeMarginInBodyOffset=i.offsetTop!==1,a.getComputedStyle&&(b.pixelPosition=(a.getComputedStyle(d,null)||{}).top!=="1%",b.boxSizingReliable=(a.getComputedStyle(d,null)||{width:"4px"}).width==="4px",g=e.createElement("div"),g.style.cssText=d.style.cssText=h,g.style.marginRight=g.style.width="0",d.style.width="1px",d.appendChild(g),b.reliableMarginRight=!parseFloat((a.getComputedStyle(g,null)||{}).marginRight)),typeof d.style.zoom!="undefined"&&(d.innerHTML="",d.style.cssText=h+"width:1px;padding:1px;display:inline;zoom:1",b.inlineBlockNeedsLayout=d.offsetWidth===3,d.style.display="block",d.style.overflow="visible",d.innerHTML="<div></div>",d.firstChild.style.width="5px",b.shrinkWrapBlocks=d.offsetWidth!==3,c.style.zoom=1),i.removeChild(c),c=d=f=g=null}),i.removeChild(n),c=d=f=g=h=i=n=null,b}();var H=/(?:\{[\s\S]*\}|\[[\s\S]*\])$/,I=/([A-Z])/g;p.extend({cache:{},deletedIds:[],uuid:0,expando:"jQuery"+(p.fn.jquery+Math.random()).replace(/\D/g,""),noData:{embed:!0,object:"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",applet:!0},hasData:function(a){return a=a.nodeType?p.cache[a[p.expando]]:a[p.expando],!!a&&!K(a)},data:function(a,c,d,e){if(!p.acceptData(a))return;var f,g,h=p.expando,i=typeof c=="string",j=a.nodeType,k=j?p.cache:a,l=j?a[h]:a[h]&&h;if((!l||!k[l]||!e&&!k[l].data)&&i&&d===b)return;l||(j?a[h]=l=p.deletedIds.pop()||++p.uuid:l=h),k[l]||(k[l]={},j||(k[l].toJSON=p.noop));if(typeof c=="object"||typeof c=="function")e?k[l]=p.extend(k[l],c):k[l].data=p.extend(k[l].data,c);return f=k[l],e||(f.data||(f.data={}),f=f.data),d!==b&&(f[p.camelCase(c)]=d),i?(g=f[c],g==null&&(g=f[p.camelCase(c)])):g=f,g},removeData:function(a,b,c){if(!p.acceptData(a))return;var d,e,f,g=a.nodeType,h=g?p.cache:a,i=g?a[p.expando]:p.expando;if(!h[i])return;if(b){d=c?h[i]:h[i].data;if(d){p.isArray(b)||(b in d?b=[b]:(b=p.camelCase(b),b in d?b=[b]:b=b.split(" ")));for(e=0,f=b.length;e<f;e++)delete d[b[e]];if(!(c?K:p.isEmptyObject)(d))return}}if(!c){delete h[i].data;if(!K(h[i]))return}g?p.cleanData([a],!0):p.support.deleteExpando||h!=h.window?delete h[i]:h[i]=null},_data:function(a,b,c){return p.data(a,b,c,!0)},acceptData:function(a){var b=a.nodeName&&p.noData[a.nodeName.toLowerCase()];return!b||b!==!0&&a.getAttribute("classid")===b}}),p.fn.extend({data:function(a,c){var d,e,f,g,h,i=this[0],j=0,k=null;if(a===b){if(this.length){k=p.data(i);if(i.nodeType===1&&!p._data(i,"parsedAttrs")){f=i.attributes;for(h=f.length;j<h;j++)g=f[j].name,g.indexOf("data-")===0&&(g=p.camelCase(g.substring(5)),J(i,g,k[g]));p._data(i,"parsedAttrs",!0)}}return k}return typeof a=="object"?this.each(function(){p.data(this,a)}):(d=a.split(".",2),d[1]=d[1]?"."+d[1]:"",e=d[1]+"!",p.access(this,function(c){if(c===b)return k=this.triggerHandler("getData"+e,[d[0]]),k===b&&i&&(k=p.data(i,a),k=J(i,a,k)),k===b&&d[1]?this.data(d[0]):k;d[1]=c,this.each(function(){var b=p(this);b.triggerHandler("setData"+e,d),p.data(this,a,c),b.triggerHandler("changeData"+e,d)})},null,c,arguments.length>1,null,!1))},removeData:function(a){return this.each(function(){p.removeData(this,a)})}}),p.extend({queue:function(a,b,c){var d;if(a)return b=(b||"fx")+"queue",d=p._data(a,b),c&&(!d||p.isArray(c)?d=p._data(a,b,p.makeArray(c)):d.push(c)),d||[]},dequeue:function(a,b){b=b||"fx";var c=p.queue(a,b),d=c.length,e=c.shift(),f=p._queueHooks(a,b),g=function(){p.dequeue(a,b)};e==="inprogress"&&(e=c.shift(),d--),e&&(b==="fx"&&c.unshift("inprogress"),delete f.stop,e.call(a,g,f)),!d&&f&&f.empty.fire()},_queueHooks:function(a,b){var c=b+"queueHooks";return p._data(a,c)||p._data(a,c,{empty:p.Callbacks("once memory").add(function(){p.removeData(a,b+"queue",!0),p.removeData(a,c,!0)})})}}),p.fn.extend({queue:function(a,c){var d=2;return typeof a!="string"&&(c=a,a="fx",d--),arguments.length<d?p.queue(this[0],a):c===b?this:this.each(function(){var b=p.queue(this,a,c);p._queueHooks(this,a),a==="fx"&&b[0]!=="inprogress"&&p.dequeue(this,a)})},dequeue:function(a){return this.each(function(){p.dequeue(this,a)})},delay:function(a,b){return a=p.fx?p.fx.speeds[a]||a:a,b=b||"fx",this.queue(b,function(b,c){var d=setTimeout(b,a);c.stop=function(){clearTimeout(d)}})},clearQueue:function(a){return this.queue(a||"fx",[])},promise:function(a,c){var d,e=1,f=p.Deferred(),g=this,h=this.length,i=function(){--e||f.resolveWith(g,[g])};typeof a!="string"&&(c=a,a=b),a=a||"fx";while(h--)d=p._data(g[h],a+"queueHooks"),d&&d.empty&&(e++,d.empty.add(i));return i(),f.promise(c)}});var L,M,N,O=/[\t\r\n]/g,P=/\r/g,Q=/^(?:button|input)$/i,R=/^(?:button|input|object|select|textarea)$/i,S=/^a(?:rea|)$/i,T=/^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i,U=p.support.getSetAttribute;p.fn.extend({attr:function(a,b){return p.access(this,p.attr,a,b,arguments.length>1)},removeAttr:function(a){return this.each(function(){p.removeAttr(this,a)})},prop:function(a,b){return p.access(this,p.prop,a,b,arguments.length>1)},removeProp:function(a){return a=p.propFix[a]||a,this.each(function(){try{this[a]=b,delete this[a]}catch(c){}})},addClass:function(a){var b,c,d,e,f,g,h;if(p.isFunction(a))return this.each(function(b){p(this).addClass(a.call(this,b,this.className))});if(a&&typeof a=="string"){b=a.split(s);for(c=0,d=this.length;c<d;c++){e=this[c];if(e.nodeType===1)if(!e.className&&b.length===1)e.className=a;else{f=" "+e.className+" ";for(g=0,h=b.length;g<h;g++)~f.indexOf(" "+b[g]+" ")||(f+=b[g]+" ");e.className=p.trim(f)}}}return this},removeClass:function(a){var c,d,e,f,g,h,i;if(p.isFunction(a))return this.each(function(b){p(this).removeClass(a.call(this,b,this.className))});if(a&&typeof a=="string"||a===b){c=(a||"").split(s);for(h=0,i=this.length;h<i;h++){e=this[h];if(e.nodeType===1&&e.className){d=(" "+e.className+" ").replace(O," ");for(f=0,g=c.length;f<g;f++)while(d.indexOf(" "+c[f]+" ")>-1)d=d.replace(" "+c[f]+" "," ");e.className=a?p.trim(d):""}}}return this},toggleClass:function(a,b){var c=typeof a,d=typeof b=="boolean";return p.isFunction(a)?this.each(function(c){p(this).toggleClass(a.call(this,c,this.className,b),b)}):this.each(function(){if(c==="string"){var e,f=0,g=p(this),h=b,i=a.split(s);while(e=i[f++])h=d?h:!g.hasClass(e),g[h?"addClass":"removeClass"](e)}else if(c==="undefined"||c==="boolean")this.className&&p._data(this,"__className__",this.className),this.className=this.className||a===!1?"":p._data(this,"__className__")||""})},hasClass:function(a){var b=" "+a+" ",c=0,d=this.length;for(;c<d;c++)if(this[c].nodeType===1&&(" "+this[c].className+" ").replace(O," ").indexOf(b)>-1)return!0;return!1},val:function(a){var c,d,e,f=this[0];if(!arguments.length){if(f)return c=p.valHooks[f.type]||p.valHooks[f.nodeName.toLowerCase()],c&&"get"in c&&(d=c.get(f,"value"))!==b?d:(d=f.value,typeof d=="string"?d.replace(P,""):d==null?"":d);return}return e=p.isFunction(a),this.each(function(d){var f,g=p(this);if(this.nodeType!==1)return;e?f=a.call(this,d,g.val()):f=a,f==null?f="":typeof f=="number"?f+="":p.isArray(f)&&(f=p.map(f,function(a){return a==null?"":a+""})),c=p.valHooks[this.type]||p.valHooks[this.nodeName.toLowerCase()];if(!c||!("set"in c)||c.set(this,f,"value")===b)this.value=f})}}),p.extend({valHooks:{option:{get:function(a){var b=a.attributes.value;return!b||b.specified?a.value:a.text}},select:{get:function(a){var b,c,d,e,f=a.selectedIndex,g=[],h=a.options,i=a.type==="select-one";if(f<0)return null;c=i?f:0,d=i?f+1:h.length;for(;c<d;c++){e=h[c];if(e.selected&&(p.support.optDisabled?!e.disabled:e.getAttribute("disabled")===null)&&(!e.parentNode.disabled||!p.nodeName(e.parentNode,"optgroup"))){b=p(e).val();if(i)return b;g.push(b)}}return i&&!g.length&&h.length?p(h[f]).val():g},set:function(a,b){var c=p.makeArray(b);return p(a).find("option").each(function(){this.selected=p.inArray(p(this).val(),c)>=0}),c.length||(a.selectedIndex=-1),c}}},attrFn:{},attr:function(a,c,d,e){var f,g,h,i=a.nodeType;if(!a||i===3||i===8||i===2)return;if(e&&p.isFunction(p.fn[c]))return p(a)[c](d);if(typeof a.getAttribute=="undefined")return p.prop(a,c,d);h=i!==1||!p.isXMLDoc(a),h&&(c=c.toLowerCase(),g=p.attrHooks[c]||(T.test(c)?M:L));if(d!==b){if(d===null){p.removeAttr(a,c);return}return g&&"set"in g&&h&&(f=g.set(a,d,c))!==b?f:(a.setAttribute(c,""+d),d)}return g&&"get"in g&&h&&(f=g.get(a,c))!==null?f:(f=a.getAttribute(c),f===null?b:f)},removeAttr:function(a,b){var c,d,e,f,g=0;if(b&&a.nodeType===1){d=b.split(s);for(;g<d.length;g++)e=d[g],e&&(c=p.propFix[e]||e,f=T.test(e),f||p.attr(a,e,""),a.removeAttribute(U?e:c),f&&c in a&&(a[c]=!1))}},attrHooks:{type:{set:function(a,b){if(Q.test(a.nodeName)&&a.parentNode)p.error("type property can't be changed");else if(!p.support.radioValue&&b==="radio"&&p.nodeName(a,"input")){var c=a.value;return a.setAttribute("type",b),c&&(a.value=c),b}}},value:{get:function(a,b){return L&&p.nodeName(a,"button")?L.get(a,b):b in a?a.value:null},set:function(a,b,c){if(L&&p.nodeName(a,"button"))return L.set(a,b,c);a.value=b}}},propFix:{tabindex:"tabIndex",readonly:"readOnly","for":"htmlFor","class":"className",maxlength:"maxLength",cellspacing:"cellSpacing",cellpadding:"cellPadding",rowspan:"rowSpan",colspan:"colSpan",usemap:"useMap",frameborder:"frameBorder",contenteditable:"contentEditable"},prop:function(a,c,d){var e,f,g,h=a.nodeType;if(!a||h===3||h===8||h===2)return;return g=h!==1||!p.isXMLDoc(a),g&&(c=p.propFix[c]||c,f=p.propHooks[c]),d!==b?f&&"set"in f&&(e=f.set(a,d,c))!==b?e:a[c]=d:f&&"get"in f&&(e=f.get(a,c))!==null?e:a[c]},propHooks:{tabIndex:{get:function(a){var c=a.getAttributeNode("tabindex");return c&&c.specified?parseInt(c.value,10):R.test(a.nodeName)||S.test(a.nodeName)&&a.href?0:b}}}}),M={get:function(a,c){var d,e=p.prop(a,c);return e===!0||typeof e!="boolean"&&(d=a.getAttributeNode(c))&&d.nodeValue!==!1?c.toLowerCase():b},set:function(a,b,c){var d;return b===!1?p.removeAttr(a,c):(d=p.propFix[c]||c,d in a&&(a[d]=!0),a.setAttribute(c,c.toLowerCase())),c}},U||(N={name:!0,id:!0,coords:!0},L=p.valHooks.button={get:function(a,c){var d;return d=a.getAttributeNode(c),d&&(N[c]?d.value!=="":d.specified)?d.value:b},set:function(a,b,c){var d=a.getAttributeNode(c);return d||(d=e.createAttribute(c),a.setAttributeNode(d)),d.value=b+""}},p.each(["width","height"],function(a,b){p.attrHooks[b]=p.extend(p.attrHooks[b],{set:function(a,c){if(c==="")return a.setAttribute(b,"auto"),c}})}),p.attrHooks.contenteditable={get:L.get,set:function(a,b,c){b===""&&(b="false"),L.set(a,b,c)}}),p.support.hrefNormalized||p.each(["href","src","width","height"],function(a,c){p.attrHooks[c]=p.extend(p.attrHooks[c],{get:function(a){var d=a.getAttribute(c,2);return d===null?b:d}})}),p.support.style||(p.attrHooks.style={get:function(a){return a.style.cssText.toLowerCase()||b},set:function(a,b){return a.style.cssText=""+b}}),p.support.optSelected||(p.propHooks.selected=p.extend(p.propHooks.selected,{get:function(a){var b=a.parentNode;return b&&(b.selectedIndex,b.parentNode&&b.parentNode.selectedIndex),null}})),p.support.enctype||(p.propFix.enctype="encoding"),p.support.checkOn||p.each(["radio","checkbox"],function(){p.valHooks[this]={get:function(a){return a.getAttribute("value")===null?"on":a.value}}}),p.each(["radio","checkbox"],function(){p.valHooks[this]=p.extend(p.valHooks[this],{set:function(a,b){if(p.isArray(b))return a.checked=p.inArray(p(a).val(),b)>=0}})});var V=/^(?:textarea|input|select)$/i,W=/^([^\.]*|)(?:\.(.+)|)$/,X=/(?:^|\s)hover(\.\S+|)\b/,Y=/^key/,Z=/^(?:mouse|contextmenu)|click/,$=/^(?:focusinfocus|focusoutblur)$/,_=function(a){return p.event.special.hover?a:a.replace(X,"mouseenter$1 mouseleave$1")};p.event={add:function(a,c,d,e,f){var g,h,i,j,k,l,m,n,o,q,r;if(a.nodeType===3||a.nodeType===8||!c||!d||!(g=p._data(a)))return;d.handler&&(o=d,d=o.handler,f=o.selector),d.guid||(d.guid=p.guid++),i=g.events,i||(g.events=i={}),h=g.handle,h||(g.handle=h=function(a){return typeof p!="undefined"&&(!a||p.event.triggered!==a.type)?p.event.dispatch.apply(h.elem,arguments):b},h.elem=a),c=p.trim(_(c)).split(" ");for(j=0;j<c.length;j++){k=W.exec(c[j])||[],l=k[1],m=(k[2]||"").split(".").sort(),r=p.event.special[l]||{},l=(f?r.delegateType:r.bindType)||l,r=p.event.special[l]||{},n=p.extend({type:l,origType:k[1],data:e,handler:d,guid:d.guid,selector:f,namespace:m.join(".")},o),q=i[l];if(!q){q=i[l]=[],q.delegateCount=0;if(!r.setup||r.setup.call(a,e,m,h)===!1)a.addEventListener?a.addEventListener(l,h,!1):a.attachEvent&&a.attachEvent("on"+l,h)}r.add&&(r.add.call(a,n),n.handler.guid||(n.handler.guid=d.guid)),f?q.splice(q.delegateCount++,0,n):q.push(n),p.event.global[l]=!0}a=null},global:{},remove:function(a,b,c,d,e){var f,g,h,i,j,k,l,m,n,o,q,r=p.hasData(a)&&p._data(a);if(!r||!(m=r.events))return;b=p.trim(_(b||"")).split(" ");for(f=0;f<b.length;f++){g=W.exec(b[f])||[],h=i=g[1],j=g[2];if(!h){for(h in m)p.event.remove(a,h+b[f],c,d,!0);continue}n=p.event.special[h]||{},h=(d?n.delegateType:n.bindType)||h,o=m[h]||[],k=o.length,j=j?new RegExp("(^|\\.)"+j.split(".").sort().join("\\.(?:.*\\.|)")+"(\\.|$)"):null;for(l=0;l<o.length;l++)q=o[l],(e||i===q.origType)&&(!c||c.guid===q.guid)&&(!j||j.test(q.namespace))&&(!d||d===q.selector||d==="**"&&q.selector)&&(o.splice(l--,1),q.selector&&o.delegateCount--,n.remove&&n.remove.call(a,q));o.length===0&&k!==o.length&&((!n.teardown||n.teardown.call(a,j,r.handle)===!1)&&p.removeEvent(a,h,r.handle),delete m[h])}p.isEmptyObject(m)&&(delete r.handle,p.removeData(a,"events",!0))},customEvent:{getData:!0,setData:!0,changeData:!0},trigger:function(c,d,f,g){if(!f||f.nodeType!==3&&f.nodeType!==8){var h,i,j,k,l,m,n,o,q,r,s=c.type||c,t=[];if($.test(s+p.event.triggered))return;s.indexOf("!")>=0&&(s=s.slice(0,-1),i=!0),s.indexOf(".")>=0&&(t=s.split("."),s=t.shift(),t.sort());if((!f||p.event.customEvent[s])&&!p.event.global[s])return;c=typeof c=="object"?c[p.expando]?c:new p.Event(s,c):new p.Event(s),c.type=s,c.isTrigger=!0,c.exclusive=i,c.namespace=t.join("."),c.namespace_re=c.namespace?new RegExp("(^|\\.)"+t.join("\\.(?:.*\\.|)")+"(\\.|$)"):null,m=s.indexOf(":")<0?"on"+s:"";if(!f){h=p.cache;for(j in h)h[j].events&&h[j].events[s]&&p.event.trigger(c,d,h[j].handle.elem,!0);return}c.result=b,c.target||(c.target=f),d=d!=null?p.makeArray(d):[],d.unshift(c),n=p.event.special[s]||{};if(n.trigger&&n.trigger.apply(f,d)===!1)return;q=[[f,n.bindType||s]];if(!g&&!n.noBubble&&!p.isWindow(f)){r=n.delegateType||s,k=$.test(r+s)?f:f.parentNode;for(l=f;k;k=k.parentNode)q.push([k,r]),l=k;l===(f.ownerDocument||e)&&q.push([l.defaultView||l.parentWindow||a,r])}for(j=0;j<q.length&&!c.isPropagationStopped();j++)k=q[j][0],c.type=q[j][1],o=(p._data(k,"events")||{})[c.type]&&p._data(k,"handle"),o&&o.apply(k,d),o=m&&k[m],o&&p.acceptData(k)&&o.apply(k,d)===!1&&c.preventDefault();return c.type=s,!g&&!c.isDefaultPrevented()&&(!n._default||n._default.apply(f.ownerDocument,d)===!1)&&(s!=="click"||!p.nodeName(f,"a"))&&p.acceptData(f)&&m&&f[s]&&(s!=="focus"&&s!=="blur"||c.target.offsetWidth!==0)&&!p.isWindow(f)&&(l=f[m],l&&(f[m]=null),p.event.triggered=s,f[s](),p.event.triggered=b,l&&(f[m]=l)),c.result}return},dispatch:function(c){c=p.event.fix(c||a.event);var d,e,f,g,h,i,j,k,l,m,n=(p._data(this,"events")||{})[c.type]||[],o=n.delegateCount,q=[].slice.call(arguments),r=!c.exclusive&&!c.namespace,s=p.event.special[c.type]||{},t=[];q[0]=c,c.delegateTarget=this;if(s.preDispatch&&s.preDispatch.call(this,c)===!1)return;if(o&&(!c.button||c.type!=="click"))for(f=c.target;f!=this;f=f.parentNode||this)if(f.disabled!==!0||c.type!=="click"){h={},j=[];for(d=0;d<o;d++)k=n[d],l=k.selector,h[l]===b&&(h[l]=p(l,this).index(f)>=0),h[l]&&j.push(k);j.length&&t.push({elem:f,matches:j})}n.length>o&&t.push({elem:this,matches:n.slice(o)});for(d=0;d<t.length&&!c.isPropagationStopped();d++){i=t[d],c.currentTarget=i.elem;for(e=0;e<i.matches.length&&!c.isImmediatePropagationStopped();e++){k=i.matches[e];if(r||!c.namespace&&!k.namespace||c.namespace_re&&c.namespace_re.test(k.namespace))c.data=k.data,c.handleObj=k,g=((p.event.special[k.origType]||{}).handle||k.handler).apply(i.elem,q),g!==b&&(c.result=g,g===!1&&(c.preventDefault(),c.stopPropagation()))}}return s.postDispatch&&s.postDispatch.call(this,c),c.result},props:"attrChange attrName relatedNode srcElement altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),fixHooks:{},keyHooks:{props:"char charCode key keyCode".split(" "),filter:function(a,b){return a.which==null&&(a.which=b.charCode!=null?b.charCode:b.keyCode),a}},mouseHooks:{props:"button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),filter:function(a,c){var d,f,g,h=c.button,i=c.fromElement;return a.pageX==null&&c.clientX!=null&&(d=a.target.ownerDocument||e,f=d.documentElement,g=d.body,a.pageX=c.clientX+(f&&f.scrollLeft||g&&g.scrollLeft||0)-(f&&f.clientLeft||g&&g.clientLeft||0),a.pageY=c.clientY+(f&&f.scrollTop||g&&g.scrollTop||0)-(f&&f.clientTop||g&&g.clientTop||0)),!a.relatedTarget&&i&&(a.relatedTarget=i===a.target?c.toElement:i),!a.which&&h!==b&&(a.which=h&1?1:h&2?3:h&4?2:0),a}},fix:function(a){if(a[p.expando])return a;var b,c,d=a,f=p.event.fixHooks[a.type]||{},g=f.props?this.props.concat(f.props):this.props;a=p.Event(d);for(b=g.length;b;)c=g[--b],a[c]=d[c];return a.target||(a.target=d.srcElement||e),a.target.nodeType===3&&(a.target=a.target.parentNode),a.metaKey=!!a.metaKey,f.filter?f.filter(a,d):a},special:{load:{noBubble:!0},focus:{delegateType:"focusin"},blur:{delegateType:"focusout"},beforeunload:{setup:function(a,b,c){p.isWindow(this)&&(this.onbeforeunload=c)},teardown:function(a,b){this.onbeforeunload===b&&(this.onbeforeunload=null)}}},simulate:function(a,b,c,d){var e=p.extend(new p.Event,c,{type:a,isSimulated:!0,originalEvent:{}});d?p.event.trigger(e,null,b):p.event.dispatch.call(b,e),e.isDefaultPrevented()&&c.preventDefault()}},p.event.handle=p.event.dispatch,p.removeEvent=e.removeEventListener?function(a,b,c){a.removeEventListener&&a.removeEventListener(b,c,!1)}:function(a,b,c){var d="on"+b;a.detachEvent&&(typeof a[d]=="undefined"&&(a[d]=null),a.detachEvent(d,c))},p.Event=function(a,b){if(this instanceof p.Event)a&&a.type?(this.originalEvent=a,this.type=a.type,this.isDefaultPrevented=a.defaultPrevented||a.returnValue===!1||a.getPreventDefault&&a.getPreventDefault()?bb:ba):this.type=a,b&&p.extend(this,b),this.timeStamp=a&&a.timeStamp||p.now(),this[p.expando]=!0;else return new p.Event(a,b)},p.Event.prototype={preventDefault:function(){this.isDefaultPrevented=bb;var a=this.originalEvent;if(!a)return;a.preventDefault?a.preventDefault():a.returnValue=!1},stopPropagation:function(){this.isPropagationStopped=bb;var a=this.originalEvent;if(!a)return;a.stopPropagation&&a.stopPropagation(),a.cancelBubble=!0},stopImmediatePropagation:function(){this.isImmediatePropagationStopped=bb,this.stopPropagation()},isDefaultPrevented:ba,isPropagationStopped:ba,isImmediatePropagationStopped:ba},p.each({mouseenter:"mouseover",mouseleave:"mouseout"},function(a,b){p.event.special[a]={delegateType:b,bindType:b,handle:function(a){var c,d=this,e=a.relatedTarget,f=a.handleObj,g=f.selector;if(!e||e!==d&&!p.contains(d,e))a.type=f.origType,c=f.handler.apply(this,arguments),a.type=b;return c}}}),p.support.submitBubbles||(p.event.special.submit={setup:function(){if(p.nodeName(this,"form"))return!1;p.event.add(this,"click._submit keypress._submit",function(a){var c=a.target,d=p.nodeName(c,"input")||p.nodeName(c,"button")?c.form:b;d&&!p._data(d,"_submit_attached")&&(p.event.add(d,"submit._submit",function(a){a._submit_bubble=!0}),p._data(d,"_submit_attached",!0))})},postDispatch:function(a){a._submit_bubble&&(delete a._submit_bubble,this.parentNode&&!a.isTrigger&&p.event.simulate("submit",this.parentNode,a,!0))},teardown:function(){if(p.nodeName(this,"form"))return!1;p.event.remove(this,"._submit")}}),p.support.changeBubbles||(p.event.special.change={setup:function(){if(V.test(this.nodeName)){if(this.type==="checkbox"||this.type==="radio")p.event.add(this,"propertychange._change",function(a){a.originalEvent.propertyName==="checked"&&(this._just_changed=!0)}),p.event.add(this,"click._change",function(a){this._just_changed&&!a.isTrigger&&(this._just_changed=!1),p.event.simulate("change",this,a,!0)});return!1}p.event.add(this,"beforeactivate._change",function(a){var b=a.target;V.test(b.nodeName)&&!p._data(b,"_change_attached")&&(p.event.add(b,"change._change",function(a){this.parentNode&&!a.isSimulated&&!a.isTrigger&&p.event.simulate("change",this.parentNode,a,!0)}),p._data(b,"_change_attached",!0))})},handle:function(a){var b=a.target;if(this!==b||a.isSimulated||a.isTrigger||b.type!=="radio"&&b.type!=="checkbox")return a.handleObj.handler.apply(this,arguments)},teardown:function(){return p.event.remove(this,"._change"),!V.test(this.nodeName)}}),p.support.focusinBubbles||p.each({focus:"focusin",blur:"focusout"},function(a,b){var c=0,d=function(a){p.event.simulate(b,a.target,p.event.fix(a),!0)};p.event.special[b]={setup:function(){c++===0&&e.addEventListener(a,d,!0)},teardown:function(){--c===0&&e.removeEventListener(a,d,!0)}}}),p.fn.extend({on:function(a,c,d,e,f){var g,h;if(typeof a=="object"){typeof c!="string"&&(d=d||c,c=b);for(h in a)this.on(h,c,d,a[h],f);return this}d==null&&e==null?(e=c,d=c=b):e==null&&(typeof c=="string"?(e=d,d=b):(e=d,d=c,c=b));if(e===!1)e=ba;else if(!e)return this;return f===1&&(g=e,e=function(a){return p().off(a),g.apply(this,arguments)},e.guid=g.guid||(g.guid=p.guid++)),this.each(function(){p.event.add(this,a,e,d,c)})},one:function(a,b,c,d){return this.on(a,b,c,d,1)},off:function(a,c,d){var e,f;if(a&&a.preventDefault&&a.handleObj)return e=a.handleObj,p(a.delegateTarget).off(e.namespace?e.origType+"."+e.namespace:e.origType,e.selector,e.handler),this;if(typeof a=="object"){for(f in a)this.off(f,c,a[f]);return this}if(c===!1||typeof c=="function")d=c,c=b;return d===!1&&(d=ba),this.each(function(){p.event.remove(this,a,d,c)})},bind:function(a,b,c){return this.on(a,null,b,c)},unbind:function(a,b){return this.off(a,null,b)},live:function(a,b,c){return p(this.context).on(a,this.selector,b,c),this},die:function(a,b){return p(this.context).off(a,this.selector||"**",b),this},delegate:function(a,b,c,d){return this.on(b,a,c,d)},undelegate:function(a,b,c){return arguments.length==1?this.off(a,"**"):this.off(b,a||"**",c)},trigger:function(a,b){return this.each(function(){p.event.trigger(a,b,this)})},triggerHandler:function(a,b){if(this[0])return p.event.trigger(a,b,this[0],!0)},toggle:function(a){var b=arguments,c=a.guid||p.guid++,d=0,e=function(c){var e=(p._data(this,"lastToggle"+a.guid)||0)%d;return p._data(this,"lastToggle"+a.guid,e+1),c.preventDefault(),b[e].apply(this,arguments)||!1};e.guid=c;while(d<b.length)b[d++].guid=c;return this.click(e)},hover:function(a,b){return this.mouseenter(a).mouseleave(b||a)}}),p.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "),function(a,b){p.fn[b]=function(a,c){return c==null&&(c=a,a=null),arguments.length>0?this.on(b,null,a,c):this.trigger(b)},Y.test(b)&&(p.event.fixHooks[b]=p.event.keyHooks),Z.test(b)&&(p.event.fixHooks[b]=p.event.mouseHooks)}),function(a,b){function $(a,b,c,d){c=c||[],b=b||q;var e,f,g,j,k=b.nodeType;if(k!==1&&k!==9)return[];if(!a||typeof a!="string")return c;g=h(b);if(!g&&!d)if(e=L.exec(a))if(j=e[1]){if(k===9){f=b.getElementById(j);if(!f||!f.parentNode)return c;if(f.id===j)return c.push(f),c}else if(b.ownerDocument&&(f=b.ownerDocument.getElementById(j))&&i(b,f)&&f.id===j)return c.push(f),c}else{if(e[2])return u.apply(c,t.call(b.getElementsByTagName(a),0)),c;if((j=e[3])&&X&&b.getElementsByClassName)return u.apply(c,t.call(b.getElementsByClassName(j),0)),c}return bk(a,b,c,d,g)}function _(a){return function(b){var c=b.nodeName.toLowerCase();return c==="input"&&b.type===a}}function ba(a){return function(b){var c=b.nodeName.toLowerCase();return(c==="input"||c==="button")&&b.type===a}}function bb(a,b,c){if(a===b)return c;var d=a.nextSibling;while(d){if(d===b)return-1;d=d.nextSibling}return 1}function bc(a,b,c,d){var e,g,h,i,j,k,l,m,n,p,r=!c&&b!==q,s=(r?"<s>":"")+a.replace(H,"$1<s>"),u=y[o][s];if(u)return d?0:t.call(u,0);j=a,k=[],m=0,n=f.preFilter,p=f.filter;while(j){if(!e||(g=I.exec(j)))g&&(j=j.slice(g[0].length),h.selector=l),k.push(h=[]),l="",r&&(j=" "+j);e=!1;if(g=J.exec(j))l+=g[0],j=j.slice(g[0].length),e=h.push({part:g.pop().replace(H," "),string:g[0],captures:g});for(i in p)(g=S[i].exec(j))&&(!n[i]||(g=n[i](g,b,c)))&&(l+=g[0],j=j.slice(g[0].length),e=h.push({part:i,string:g.shift(),captures:g}));if(!e)break}return l&&(h.selector=l),d?j.length:j?$.error(a):t.call(y(s,k),0)}function bd(a,b,e,f){var g=b.dir,h=s++;return a||(a=function(a){return a===e}),b.first?function(b){while(b=b[g])if(b.nodeType===1)return a(b)&&b}:f?function(b){while(b=b[g])if(b.nodeType===1&&a(b))return b}:function(b){var e,f=h+"."+c,i=f+"."+d;while(b=b[g])if(b.nodeType===1){if((e=b[o])===i)return b.sizset;if(typeof e=="string"&&e.indexOf(f)===0){if(b.sizset)return b}else{b[o]=i;if(a(b))return b.sizset=!0,b;b.sizset=!1}}}}function be(a,b){return a?function(c){var d=b(c);return d&&a(d===!0?c:d)}:b}function bf(a,b,c){var d,e,g=0;for(;d=a[g];g++)f.relative[d.part]?e=bd(e,f.relative[d.part],b,c):e=be(e,f.filter[d.part].apply(null,d.captures.concat(b,c)));return e}function bg(a){return function(b){var c,d=0;for(;c=a[d];d++)if(c(b))return!0;return!1}}function bh(a,b,c,d){var e=0,f=b.length;for(;e<f;e++)$(a,b[e],c,d)}function bi(a,b,c,d,e,g){var h,i=f.setFilters[b.toLowerCase()];return i||$.error(b),(a||!(h=e))&&bh(a||"*",d,h=[],e),h.length>0?i(h,c,g):[]}function bj(a,c,d,e){var f,g,h,i,j,k,l,m,n,o,p,q,r,s=0,t=a.length,v=S.POS,w=new RegExp("^"+v.source+"(?!"+A+")","i"),x=function(){var a=1,c=arguments.length-2;for(;a<c;a++)arguments[a]===b&&(n[a]=b)};for(;s<t;s++){f=a[s],g="",m=e;for(h=0,i=f.length;h<i;h++){j=f[h],k=j.string;if(j.part==="PSEUDO"){v.exec(""),l=0;while(n=v.exec(k)){o=!0,p=v.lastIndex=n.index+n[0].length;if(p>l){g+=k.slice(l,n.index),l=p,q=[c],J.test(g)&&(m&&(q=m),m=e);if(r=O.test(g))g=g.slice(0,-5).replace(J,"$&*"),l++;n.length>1&&n[0].replace(w,x),m=bi(g,n[1],n[2],q,m,r)}g=""}}o||(g+=k),o=!1}g?J.test(g)?bh(g,m||[c],d,e):$(g,c,d,e?e.concat(m):m):u.apply(d,m)}return t===1?d:$.uniqueSort(d)}function bk(a,b,e,g,h){a=a.replace(H,"$1");var i,k,l,m,n,o,p,q,r,s,v=bc(a,b,h),w=b.nodeType;if(S.POS.test(a))return bj(v,b,e,g);if(g)i=t.call(g,0);else if(v.length===1){if((o=t.call(v[0],0)).length>2&&(p=o[0]).part==="ID"&&w===9&&!h&&f.relative[o[1].part]){b=f.find.ID(p.captures[0].replace(R,""),b,h)[0];if(!b)return e;a=a.slice(o.shift().string.length)}r=(v=N.exec(o[0].string))&&!v.index&&b.parentNode||b,q="";for(n=o.length-1;n>=0;n--){p=o[n],s=p.part,q=p.string+q;if(f.relative[s])break;if(f.order.test(s)){i=f.find[s](p.captures[0].replace(R,""),r,h);if(i==null)continue;a=a.slice(0,a.length-q.length)+q.replace(S[s],""),a||u.apply(e,t.call(i,0));break}}}if(a){k=j(a,b,h),c=k.dirruns++,i==null&&(i=f.find.TAG("*",N.test(a)&&b.parentNode||b));for(n=0;m=i[n];n++)d=k.runs++,k(m)&&e.push(m)}return e}var c,d,e,f,g,h,i,j,k,l,m=!0,n="undefined",o=("sizcache"+Math.random()).replace(".",""),q=a.document,r=q.documentElement,s=0,t=[].slice,u=[].push,v=function(a,b){return a[o]=b||!0,a},w=function(){var a={},b=[];return v(function(c,d){return b.push(c)>f.cacheLength&&delete a[b.shift()],a[c]=d},a)},x=w(),y=w(),z=w(),A="[\\x20\\t\\r\\n\\f]",B="(?:\\\\.|[-\\w]|[^\\x00-\\xa0])+",C=B.replace("w","w#"),D="([*^$|!~]?=)",E="\\["+A+"*("+B+")"+A+"*(?:"+D+A+"*(?:(['\"])((?:\\\\.|[^\\\\])*?)\\3|("+C+")|)|)"+A+"*\\]",F=":("+B+")(?:\\((?:(['\"])((?:\\\\.|[^\\\\])*?)\\2|([^()[\\]]*|(?:(?:"+E+")|[^:]|\\\\.)*|.*))\\)|)",G=":(nth|eq|gt|lt|first|last|even|odd)(?:\\(((?:-\\d)?\\d*)\\)|)(?=[^-]|$)",H=new RegExp("^"+A+"+|((?:^|[^\\\\])(?:\\\\.)*)"+A+"+$","g"),I=new RegExp("^"+A+"*,"+A+"*"),J=new RegExp("^"+A+"*([\\x20\\t\\r\\n\\f>+~])"+A+"*"),K=new RegExp(F),L=/^(?:#([\w\-]+)|(\w+)|\.([\w\-]+))$/,M=/^:not/,N=/[\x20\t\r\n\f]*[+~]/,O=/:not\($/,P=/h\d/i,Q=/input|select|textarea|button/i,R=/\\(?!\\)/g,S={ID:new RegExp("^#("+B+")"),CLASS:new RegExp("^\\.("+B+")"),NAME:new RegExp("^\\[name=['\"]?("+B+")['\"]?\\]"),TAG:new RegExp("^("+B.replace("w","w*")+")"),ATTR:new RegExp("^"+E),PSEUDO:new RegExp("^"+F),CHILD:new RegExp("^:(only|nth|last|first)-child(?:\\("+A+"*(even|odd|(([+-]|)(\\d*)n|)"+A+"*(?:([+-]|)"+A+"*(\\d+)|))"+A+"*\\)|)","i"),POS:new RegExp(G,"ig"),needsContext:new RegExp("^"+A+"*[>+~]|"+G,"i")},T=function(a){var b=q.createElement("div");try{return a(b)}catch(c){return!1}finally{b=null}},U=T(function(a){return a.appendChild(q.createComment("")),!a.getElementsByTagName("*").length}),V=T(function(a){return a.innerHTML="<a href='#'></a>",a.firstChild&&typeof a.firstChild.getAttribute!==n&&a.firstChild.getAttribute("href")==="#"}),W=T(function(a){a.innerHTML="<select></select>";var b=typeof a.lastChild.getAttribute("multiple");return b!=="boolean"&&b!=="string"}),X=T(function(a){return a.innerHTML="<div class='hidden e'></div><div class='hidden'></div>",!a.getElementsByClassName||!a.getElementsByClassName("e").length?!1:(a.lastChild.className="e",a.getElementsByClassName("e").length===2)}),Y=T(function(a){a.id=o+0,a.innerHTML="<a name='"+o+"'></a><div name='"+o+"'></div>",r.insertBefore(a,r.firstChild);var b=q.getElementsByName&&q.getElementsByName(o).length===2+q.getElementsByName(o+0).length;return e=!q.getElementById(o),r.removeChild(a),b});try{t.call(r.childNodes,0)[0].nodeType}catch(Z){t=function(a){var b,c=[];for(;b=this[a];a++)c.push(b);return c}}$.matches=function(a,b){return $(a,null,null,b)},$.matchesSelector=function(a,b){return $(b,null,null,[a]).length>0},g=$.getText=function(a){var b,c="",d=0,e=a.nodeType;if(e){if(e===1||e===9||e===11){if(typeof a.textContent=="string")return a.textContent;for(a=a.firstChild;a;a=a.nextSibling)c+=g(a)}else if(e===3||e===4)return a.nodeValue}else for(;b=a[d];d++)c+=g(b);return c},h=$.isXML=function(a){var b=a&&(a.ownerDocument||a).documentElement;return b?b.nodeName!=="HTML":!1},i=$.contains=r.contains?function(a,b){var c=a.nodeType===9?a.documentElement:a,d=b&&b.parentNode;return a===d||!!(d&&d.nodeType===1&&c.contains&&c.contains(d))}:r.compareDocumentPosition?function(a,b){return b&&!!(a.compareDocumentPosition(b)&16)}:function(a,b){while(b=b.parentNode)if(b===a)return!0;return!1},$.attr=function(a,b){var c,d=h(a);return d||(b=b.toLowerCase()),f.attrHandle[b]?f.attrHandle[b](a):W||d?a.getAttribute(b):(c=a.getAttributeNode(b),c?typeof a[b]=="boolean"?a[b]?b:null:c.specified?c.value:null:null)},f=$.selectors={cacheLength:50,createPseudo:v,match:S,order:new RegExp("ID|TAG"+(Y?"|NAME":"")+(X?"|CLASS":"")),attrHandle:V?{}:{href:function(a){return a.getAttribute("href",2)},type:function(a){return a.getAttribute("type")}},find:{ID:e?function(a,b,c){if(typeof b.getElementById!==n&&!c){var d=b.getElementById(a);return d&&d.parentNode?[d]:[]}}:function(a,c,d){if(typeof c.getElementById!==n&&!d){var e=c.getElementById(a);return e?e.id===a||typeof e.getAttributeNode!==n&&e.getAttributeNode("id").value===a?[e]:b:[]}},TAG:U?function(a,b){if(typeof b.getElementsByTagName!==n)return b.getElementsByTagName(a)}:function(a,b){var c=b.getElementsByTagName(a);if(a==="*"){var d,e=[],f=0;for(;d=c[f];f++)d.nodeType===1&&e.push(d);return e}return c},NAME:function(a,b){if(typeof b.getElementsByName!==n)return b.getElementsByName(name)},CLASS:function(a,b,c){if(typeof b.getElementsByClassName!==n&&!c)return b.getElementsByClassName(a)}},relative:{">":{dir:"parentNode",first:!0}," ":{dir:"parentNode"},"+":{dir:"previousSibling",first:!0},"~":{dir:"previousSibling"}},preFilter:{ATTR:function(a){return a[1]=a[1].replace(R,""),a[3]=(a[4]||a[5]||"").replace(R,""),a[2]==="~="&&(a[3]=" "+a[3]+" "),a.slice(0,4)},CHILD:function(a){return a[1]=a[1].toLowerCase(),a[1]==="nth"?(a[2]||$.error(a[0]),a[3]=+(a[3]?a[4]+(a[5]||1):2*(a[2]==="even"||a[2]==="odd")),a[4]=+(a[6]+a[7]||a[2]==="odd")):a[2]&&$.error(a[0]),a},PSEUDO:function(a,b,c){var d,e;if(S.CHILD.test(a[0]))return null;if(a[3])a[2]=a[3];else if(d=a[4])K.test(d)&&(e=bc(d,b,c,!0))&&(e=d.indexOf(")",d.length-e)-d.length)&&(d=d.slice(0,e),a[0]=a[0].slice(0,e)),a[2]=d;return a.slice(0,3)}},filter:{ID:e?function(a){return a=a.replace(R,""),function(b){return b.getAttribute("id")===a}}:function(a){return a=a.replace(R,""),function(b){var c=typeof b.getAttributeNode!==n&&b.getAttributeNode("id");return c&&c.value===a}},TAG:function(a){return a==="*"?function(){return!0}:(a=a.replace(R,"").toLowerCase(),function(b){return b.nodeName&&b.nodeName.toLowerCase()===a})},CLASS:function(a){var b=x[o][a];return b||(b=x(a,new RegExp("(^|"+A+")"+a+"("+A+"|$)"))),function(a){return b.test(a.className||typeof a.getAttribute!==n&&a.getAttribute("class")||"")}},ATTR:function(a,b,c){return b?function(d){var e=$.attr(d,a),f=e+"";if(e==null)return b==="!=";switch(b){case"=":return f===c;case"!=":return f!==c;case"^=":return c&&f.indexOf(c)===0;case"*=":return c&&f.indexOf(c)>-1;case"$=":return c&&f.substr(f.length-c.length)===c;case"~=":return(" "+f+" ").indexOf(c)>-1;case"|=":return f===c||f.substr(0,c.length+1)===c+"-"}}:function(b){return $.attr(b,a)!=null}},CHILD:function(a,b,c,d){if(a==="nth"){var e=s++;return function(a){var b,f,g=0,h=a;if(c===1&&d===0)return!0;b=a.parentNode;if(b&&(b[o]!==e||!a.sizset)){for(h=b.firstChild;h;h=h.nextSibling)if(h.nodeType===1){h.sizset=++g;if(h===a)break}b[o]=e}return f=a.sizset-d,c===0?f===0:f%c===0&&f/c>=0}}return function(b){var c=b;switch(a){case"only":case"first":while(c=c.previousSibling)if(c.nodeType===1)return!1;if(a==="first")return!0;c=b;case"last":while(c=c.nextSibling)if(c.nodeType===1)return!1;return!0}}},PSEUDO:function(a,b,c,d){var e,g=f.pseudos[a]||f.pseudos[a.toLowerCase()];return g||$.error("unsupported pseudo: "+a),g[o]?g(b,c,d):g.length>1?(e=[a,a,"",b],function(a){return g(a,0,e)}):g}},pseudos:{not:v(function(a,b,c){var d=j(a.replace(H,"$1"),b,c);return function(a){return!d(a)}}),enabled:function(a){return a.disabled===!1},disabled:function(a){return a.disabled===!0},checked:function(a){var b=a.nodeName.toLowerCase();return b==="input"&&!!a.checked||b==="option"&&!!a.selected},selected:function(a){return a.parentNode&&a.parentNode.selectedIndex,a.selected===!0},parent:function(a){return!f.pseudos.empty(a)},empty:function(a){var b;a=a.firstChild;while(a){if(a.nodeName>"@"||(b=a.nodeType)===3||b===4)return!1;a=a.nextSibling}return!0},contains:v(function(a){return function(b){return(b.textContent||b.innerText||g(b)).indexOf(a)>-1}}),has:v(function(a){return function(b){return $(a,b).length>0}}),header:function(a){return P.test(a.nodeName)},text:function(a){var b,c;return a.nodeName.toLowerCase()==="input"&&(b=a.type)==="text"&&((c=a.getAttribute("type"))==null||c.toLowerCase()===b)},radio:_("radio"),checkbox:_("checkbox"),file:_("file"),password:_("password"),image:_("image"),submit:ba("submit"),reset:ba("reset"),button:function(a){var b=a.nodeName.toLowerCase();return b==="input"&&a.type==="button"||b==="button"},input:function(a){return Q.test(a.nodeName)},focus:function(a){var b=a.ownerDocument;return a===b.activeElement&&(!b.hasFocus||b.hasFocus())&&(!!a.type||!!a.href)},active:function(a){return a===a.ownerDocument.activeElement}},setFilters:{first:function(a,b,c){return c?a.slice(1):[a[0]]},last:function(a,b,c){var d=a.pop();return c?a:[d]},even:function(a,b,c){var d=[],e=c?1:0,f=a.length;for(;e<f;e=e+2)d.push(a[e]);return d},odd:function(a,b,c){var d=[],e=c?0:1,f=a.length;for(;e<f;e=e+2)d.push(a[e]);return d},lt:function(a,b,c){return c?a.slice(+b):a.slice(0,+b)},gt:function(a,b,c){return c?a.slice(0,+b+1):a.slice(+b+1)},eq:function(a,b,c){var d=a.splice(+b,1);return c?a:d}}},k=r.compareDocumentPosition?function(a,b){return a===b?(l=!0,0):(!a.compareDocumentPosition||!b.compareDocumentPosition?a.compareDocumentPosition:a.compareDocumentPosition(b)&4)?-1:1}:function(a,b){if(a===b)return l=!0,0;if(a.sourceIndex&&b.sourceIndex)return a.sourceIndex-b.sourceIndex;var c,d,e=[],f=[],g=a.parentNode,h=b.parentNode,i=g;if(g===h)return bb(a,b);if(!g)return-1;if(!h)return 1;while(i)e.unshift(i),i=i.parentNode;i=h;while(i)f.unshift(i),i=i.parentNode;c=e.length,d=f.length;for(var j=0;j<c&&j<d;j++)if(e[j]!==f[j])return bb(e[j],f[j]);return j===c?bb(a,f[j],-1):bb(e[j],b,1)},[0,0].sort(k),m=!l,$.uniqueSort=function(a){var b,c=1;l=m,a.sort(k);if(l)for(;b=a[c];c++)b===a[c-1]&&a.splice(c--,1);return a},$.error=function(a){throw new Error("Syntax error, unrecognized expression: "+a)},j=$.compile=function(a,b,c){var d,e,f,g=z[o][a];if(g&&g.context===b)return g;d=bc(a,b,c);for(e=0,f=d.length;e<f;e++)d[e]=bf(d[e],b,c);return g=z(a,bg(d)),g.context=b,g.runs=g.dirruns=0,g},q.querySelectorAll&&function(){var a,b=bk,c=/'|\\/g,d=/\=[\x20\t\r\n\f]*([^'"\]]*)[\x20\t\r\n\f]*\]/g,e=[],f=[":active"],g=r.matchesSelector||r.mozMatchesSelector||r.webkitMatchesSelector||r.oMatchesSelector||r.msMatchesSelector;T(function(a){a.innerHTML="<select><option selected=''></option></select>",a.querySelectorAll("[selected]").length||e.push("\\["+A+"*(?:checked|disabled|ismap|multiple|readonly|selected|value)"),a.querySelectorAll(":checked").length||e.push(":checked")}),T(function(a){a.innerHTML="<p test=''></p>",a.querySelectorAll("[test^='']").length&&e.push("[*^$]="+A+"*(?:\"\"|'')"),a.innerHTML="<input type='hidden'/>",a.querySelectorAll(":enabled").length||e.push(":enabled",":disabled")}),e=e.length&&new RegExp(e.join("|")),bk=function(a,d,f,g,h){if(!g&&!h&&(!e||!e.test(a)))if(d.nodeType===9)try{return u.apply(f,t.call(d.querySelectorAll(a),0)),f}catch(i){}else if(d.nodeType===1&&d.nodeName.toLowerCase()!=="object"){var j,k,l,m=d.getAttribute("id"),n=m||o,p=N.test(a)&&d.parentNode||d;m?n=n.replace(c,"\\$&"):d.setAttribute("id",n),j=bc(a,d,h),n="[id='"+n+"']";for(k=0,l=j.length;k<l;k++)j[k]=n+j[k].selector;try{return u.apply(f,t.call(p.querySelectorAll(j.join(",")),0)),f}catch(i){}finally{m||d.removeAttribute("id")}}return b(a,d,f,g,h)},g&&(T(function(b){a=g.call(b,"div");try{g.call(b,"[test!='']:sizzle"),f.push(S.PSEUDO.source,S.POS.source,"!=")}catch(c){}}),f=new RegExp(f.join("|")),$.matchesSelector=function(b,c){c=c.replace(d,"='$1']");if(!h(b)&&!f.test(c)&&(!e||!e.test(c)))try{var i=g.call(b,c);if(i||a||b.document&&b.document.nodeType!==11)return i}catch(j){}return $(c,null,null,[b]).length>0})}(),f.setFilters.nth=f.setFilters.eq,f.filters=f.pseudos,$.attr=p.attr,p.find=$,p.expr=$.selectors,p.expr[":"]=p.expr.pseudos,p.unique=$.uniqueSort,p.text=$.getText,p.isXMLDoc=$.isXML,p.contains=$.contains}(a);var bc=/Until$/,bd=/^(?:parents|prev(?:Until|All))/,be=/^.[^:#\[\.,]*$/,bf=p.expr.match.needsContext,bg={children:!0,contents:!0,next:!0,prev:!0};p.fn.extend({find:function(a){var b,c,d,e,f,g,h=this;if(typeof a!="string")return p(a).filter(function(){for(b=0,c=h.length;b<c;b++)if(p.contains(h[b],this))return!0});g=this.pushStack("","find",a);for(b=0,c=this.length;b<c;b++){d=g.length,p.find(a,this[b],g);if(b>0)for(e=d;e<g.length;e++)for(f=0;f<d;f++)if(g[f]===g[e]){g.splice(e--,1);break}}return g},has:function(a){var b,c=p(a,this),d=c.length;return this.filter(function(){for(b=0;b<d;b++)if(p.contains(this,c[b]))return!0})},not:function(a){return this.pushStack(bj(this,a,!1),"not",a)},filter:function(a){return this.pushStack(bj(this,a,!0),"filter",a)},is:function(a){return!!a&&(typeof a=="string"?bf.test(a)?p(a,this.context).index(this[0])>=0:p.filter(a,this).length>0:this.filter(a).length>0)},closest:function(a,b){var c,d=0,e=this.length,f=[],g=bf.test(a)||typeof a!="string"?p(a,b||this.context):0;for(;d<e;d++){c=this[d];while(c&&c.ownerDocument&&c!==b&&c.nodeType!==11){if(g?g.index(c)>-1:p.find.matchesSelector(c,a)){f.push(c);break}c=c.parentNode}}return f=f.length>1?p.unique(f):f,this.pushStack(f,"closest",a)},index:function(a){return a?typeof a=="string"?p.inArray(this[0],p(a)):p.inArray(a.jquery?a[0]:a,this):this[0]&&this[0].parentNode?this.prevAll().length:-1},add:function(a,b){var c=typeof a=="string"?p(a,b):p.makeArray(a&&a.nodeType?[a]:a),d=p.merge(this.get(),c);return this.pushStack(bh(c[0])||bh(d[0])?d:p.unique(d))},addBack:function(a){return this.add(a==null?this.prevObject:this.prevObject.filter(a))}}),p.fn.andSelf=p.fn.addBack,p.each({parent:function(a){var b=a.parentNode;return b&&b.nodeType!==11?b:null},parents:function(a){return p.dir(a,"parentNode")},parentsUntil:function(a,b,c){return p.dir(a,"parentNode",c)},next:function(a){return bi(a,"nextSibling")},prev:function(a){return bi(a,"previousSibling")},nextAll:function(a){return p.dir(a,"nextSibling")},prevAll:function(a){return p.dir(a,"previousSibling")},nextUntil:function(a,b,c){return p.dir(a,"nextSibling",c)},prevUntil:function(a,b,c){return p.dir(a,"previousSibling",c)},siblings:function(a){return p.sibling((a.parentNode||{}).firstChild,a)},children:function(a){return p.sibling(a.firstChild)},contents:function(a){return p.nodeName(a,"iframe")?a.contentDocument||a.contentWindow.document:p.merge([],a.childNodes)}},function(a,b){p.fn[a]=function(c,d){var e=p.map(this,b,c);return bc.test(a)||(d=c),d&&typeof d=="string"&&(e=p.filter(d,e)),e=this.length>1&&!bg[a]?p.unique(e):e,this.length>1&&bd.test(a)&&(e=e.reverse()),this.pushStack(e,a,k.call(arguments).join(","))}}),p.extend({filter:function(a,b,c){return c&&(a=":not("+a+")"),b.length===1?p.find.matchesSelector(b[0],a)?[b[0]]:[]:p.find.matches(a,b)},dir:function(a,c,d){var e=[],f=a[c];while(f&&f.nodeType!==9&&(d===b||f.nodeType!==1||!p(f).is(d)))f.nodeType===1&&e.push(f),f=f[c];return e},sibling:function(a,b){var c=[];for(;a;a=a.nextSibling)a.nodeType===1&&a!==b&&c.push(a);return c}});var bl="abbr|article|aside|audio|bdi|canvas|data|datalist|details|figcaption|figure|footer|header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",bm=/ jQuery\d+="(?:null|\d+)"/g,bn=/^\s+/,bo=/<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi,bp=/<([\w:]+)/,bq=/<tbody/i,br=/<|&#?\w+;/,bs=/<(?:script|style|link)/i,bt=/<(?:script|object|embed|option|style)/i,bu=new RegExp("<(?:"+bl+")[\\s/>]","i"),bv=/^(?:checkbox|radio)$/,bw=/checked\s*(?:[^=]|=\s*.checked.)/i,bx=/\/(java|ecma)script/i,by=/^\s*<!(?:\[CDATA\[|\-\-)|[\]\-]{2}>\s*$/g,bz={option:[1,"<select multiple='multiple'>","</select>"],legend:[1,"<fieldset>","</fieldset>"],thead:[1,"<table>","</table>"],tr:[2,"<table><tbody>","</tbody></table>"],td:[3,"<table><tbody><tr>","</tr></tbody></table>"],col:[2,"<table><tbody></tbody><colgroup>","</colgroup></table>"],area:[1,"<map>","</map>"],_default:[0,"",""]},bA=bk(e),bB=bA.appendChild(e.createElement("div"));bz.optgroup=bz.option,bz.tbody=bz.tfoot=bz.colgroup=bz.caption=bz.thead,bz.th=bz.td,p.support.htmlSerialize||(bz._default=[1,"X<div>","</div>"]),p.fn.extend({text:function(a){return p.access(this,function(a){return a===b?p.text(this):this.empty().append((this[0]&&this[0].ownerDocument||e).createTextNode(a))},null,a,arguments.length)},wrapAll:function(a){if(p.isFunction(a))return this.each(function(b){p(this).wrapAll(a.call(this,b))});if(this[0]){var b=p(a,this[0].ownerDocument).eq(0).clone(!0);this[0].parentNode&&b.insertBefore(this[0]),b.map(function(){var a=this;while(a.firstChild&&a.firstChild.nodeType===1)a=a.firstChild;return a}).append(this)}return this},wrapInner:function(a){return p.isFunction(a)?this.each(function(b){p(this).wrapInner(a.call(this,b))}):this.each(function(){var b=p(this),c=b.contents();c.length?c.wrapAll(a):b.append(a)})},wrap:function(a){var b=p.isFunction(a);return this.each(function(c){p(this).wrapAll(b?a.call(this,c):a)})},unwrap:function(){return this.parent().each(function(){p.nodeName(this,"body")||p(this).replaceWith(this.childNodes)}).end()},append:function(){return this.domManip(arguments,!0,function(a){(this.nodeType===1||this.nodeType===11)&&this.appendChild(a)})},prepend:function(){return this.domManip(arguments,!0,function(a){(this.nodeType===1||this.nodeType===11)&&this.insertBefore(a,this.firstChild)})},before:function(){if(!bh(this[0]))return this.domManip(arguments,!1,function(a){this.parentNode.insertBefore(a,this)});if(arguments.length){var a=p.clean(arguments);return this.pushStack(p.merge(a,this),"before",this.selector)}},after:function(){if(!bh(this[0]))return this.domManip(arguments,!1,function(a){this.parentNode.insertBefore(a,this.nextSibling)});if(arguments.length){var a=p.clean(arguments);return this.pushStack(p.merge(this,a),"after",this.selector)}},remove:function(a,b){var c,d=0;for(;(c=this[d])!=null;d++)if(!a||p.filter(a,[c]).length)!b&&c.nodeType===1&&(p.cleanData(c.getElementsByTagName("*")),p.cleanData([c])),c.parentNode&&c.parentNode.removeChild(c);return this},empty:function(){var a,b=0;for(;(a=this[b])!=null;b++){a.nodeType===1&&p.cleanData(a.getElementsByTagName("*"));while(a.firstChild)a.removeChild(a.firstChild)}return this},clone:function(a,b){return a=a==null?!1:a,b=b==null?a:b,this.map(function(){return p.clone(this,a,b)})},html:function(a){return p.access(this,function(a){var c=this[0]||{},d=0,e=this.length;if(a===b)return c.nodeType===1?c.innerHTML.replace(bm,""):b;if(typeof a=="string"&&!bs.test(a)&&(p.support.htmlSerialize||!bu.test(a))&&(p.support.leadingWhitespace||!bn.test(a))&&!bz[(bp.exec(a)||["",""])[1].toLowerCase()]){a=a.replace(bo,"<$1></$2>");try{for(;d<e;d++)c=this[d]||{},c.nodeType===1&&(p.cleanData(c.getElementsByTagName("*")),c.innerHTML=a);c=0}catch(f){}}c&&this.empty().append(a)},null,a,arguments.length)},replaceWith:function(a){return bh(this[0])?this.length?this.pushStack(p(p.isFunction(a)?a():a),"replaceWith",a):this:p.isFunction(a)?this.each(function(b){var c=p(this),d=c.html();c.replaceWith(a.call(this,b,d))}):(typeof a!="string"&&(a=p(a).detach()),this.each(function(){var b=this.nextSibling,c=this.parentNode;p(this).remove(),b?p(b).before(a):p(c).append(a)}))},detach:function(a){return this.remove(a,!0)},domManip:function(a,c,d){a=[].concat.apply([],a);var e,f,g,h,i=0,j=a[0],k=[],l=this.length;if(!p.support.checkClone&&l>1&&typeof j=="string"&&bw.test(j))return this.each(function(){p(this).domManip(a,c,d)});if(p.isFunction(j))return this.each(function(e){var f=p(this);a[0]=j.call(this,e,c?f.html():b),f.domManip(a,c,d)});if(this[0]){e=p.buildFragment(a,this,k),g=e.fragment,f=g.firstChild,g.childNodes.length===1&&(g=f);if(f){c=c&&p.nodeName(f,"tr");for(h=e.cacheable||l-1;i<l;i++)d.call(c&&p.nodeName(this[i],"table")?bC(this[i],"tbody"):this[i],i===h?g:p.clone(g,!0,!0))}g=f=null,k.length&&p.each(k,function(a,b){b.src?p.ajax?p.ajax({url:b.src,type:"GET",dataType:"script",async:!1,global:!1,"throws":!0}):p.error("no ajax"):p.globalEval((b.text||b.textContent||b.innerHTML||"").replace(by,"")),b.parentNode&&b.parentNode.removeChild(b)})}return this}}),p.buildFragment=function(a,c,d){var f,g,h,i=a[0];return c=c||e,c=!c.nodeType&&c[0]||c,c=c.ownerDocument||c,a.length===1&&typeof i=="string"&&i.length<512&&c===e&&i.charAt(0)==="<"&&!bt.test(i)&&(p.support.checkClone||!bw.test(i))&&(p.support.html5Clone||!bu.test(i))&&(g=!0,f=p.fragments[i],h=f!==b),f||(f=c.createDocumentFragment(),p.clean(a,c,f,d),g&&(p.fragments[i]=h&&f)),{fragment:f,cacheable:g}},p.fragments={},p.each({appendTo:"append",prependTo:"prepend",insertBefore:"before",insertAfter:"after",replaceAll:"replaceWith"},function(a,b){p.fn[a]=function(c){var d,e=0,f=[],g=p(c),h=g.length,i=this.length===1&&this[0].parentNode;if((i==null||i&&i.nodeType===11&&i.childNodes.length===1)&&h===1)return g[b](this[0]),this;for(;e<h;e++)d=(e>0?this.clone(!0):this).get(),p(g[e])[b](d),f=f.concat(d);return this.pushStack(f,a,g.selector)}}),p.extend({clone:function(a,b,c){var d,e,f,g;p.support.html5Clone||p.isXMLDoc(a)||!bu.test("<"+a.nodeName+">")?g=a.cloneNode(!0):(bB.innerHTML=a.outerHTML,bB.removeChild(g=bB.firstChild));if((!p.support.noCloneEvent||!p.support.noCloneChecked)&&(a.nodeType===1||a.nodeType===11)&&!p.isXMLDoc(a)){bE(a,g),d=bF(a),e=bF(g);for(f=0;d[f];++f)e[f]&&bE(d[f],e[f])}if(b){bD(a,g);if(c){d=bF(a),e=bF(g);for(f=0;d[f];++f)bD(d[f],e[f])}}return d=e=null,g},clean:function(a,b,c,d){var f,g,h,i,j,k,l,m,n,o,q,r,s=b===e&&bA,t=[];if(!b||typeof b.createDocumentFragment=="undefined")b=e;for(f=0;(h=a[f])!=null;f++){typeof h=="number"&&(h+="");if(!h)continue;if(typeof h=="string")if(!br.test(h))h=b.createTextNode(h);else{s=s||bk(b),l=b.createElement("div"),s.appendChild(l),h=h.replace(bo,"<$1></$2>"),i=(bp.exec(h)||["",""])[1].toLowerCase(),j=bz[i]||bz._default,k=j[0],l.innerHTML=j[1]+h+j[2];while(k--)l=l.lastChild;if(!p.support.tbody){m=bq.test(h),n=i==="table"&&!m?l.firstChild&&l.firstChild.childNodes:j[1]==="<table>"&&!m?l.childNodes:[];for(g=n.length-1;g>=0;--g)p.nodeName(n[g],"tbody")&&!n[g].childNodes.length&&n[g].parentNode.removeChild(n[g])}!p.support.leadingWhitespace&&bn.test(h)&&l.insertBefore(b.createTextNode(bn.exec(h)[0]),l.firstChild),h=l.childNodes,l.parentNode.removeChild(l)}h.nodeType?t.push(h):p.merge(t,h)}l&&(h=l=s=null);if(!p.support.appendChecked)for(f=0;(h=t[f])!=null;f++)p.nodeName(h,"input")?bG(h):typeof h.getElementsByTagName!="undefined"&&p.grep(h.getElementsByTagName("input"),bG);if(c){q=function(a){if(!a.type||bx.test(a.type))return d?d.push(a.parentNode?a.parentNode.removeChild(a):a):c.appendChild(a)};for(f=0;(h=t[f])!=null;f++)if(!p.nodeName(h,"script")||!q(h))c.appendChild(h),typeof h.getElementsByTagName!="undefined"&&(r=p.grep(p.merge([],h.getElementsByTagName("script")),q),t.splice.apply(t,[f+1,0].concat(r)),f+=r.length)}return t},cleanData:function(a,b){var c,d,e,f,g=0,h=p.expando,i=p.cache,j=p.support.deleteExpando,k=p.event.special;for(;(e=a[g])!=null;g++)if(b||p.acceptData(e)){d=e[h],c=d&&i[d];if(c){if(c.events)for(f in c.events)k[f]?p.event.remove(e,f):p.removeEvent(e,f,c.handle);i[d]&&(delete i[d],j?delete e[h]:e.removeAttribute?e.removeAttribute(h):e[h]=null,p.deletedIds.push(d))}}}}),function(){var a,b;p.uaMatch=function(a){a=a.toLowerCase();var b=/(chrome)[ \/]([\w.]+)/.exec(a)||/(webkit)[ \/]([\w.]+)/.exec(a)||/(opera)(?:.*version|)[ \/]([\w.]+)/.exec(a)||/(msie) ([\w.]+)/.exec(a)||a.indexOf("compatible")<0&&/(mozilla)(?:.*? rv:([\w.]+)|)/.exec(a)||[];return{browser:b[1]||"",version:b[2]||"0"}},a=p.uaMatch(g.userAgent),b={},a.browser&&(b[a.browser]=!0,b.version=a.version),b.chrome?b.webkit=!0:b.webkit&&(b.safari=!0),p.browser=b,p.sub=function(){function a(b,c){return new a.fn.init(b,c)}p.extend(!0,a,this),a.superclass=this,a.fn=a.prototype=this(),a.fn.constructor=a,a.sub=this.sub,a.fn.init=function c(c,d){return d&&d instanceof p&&!(d instanceof a)&&(d=a(d)),p.fn.init.call(this,c,d,b)},a.fn.init.prototype=a.fn;var b=a(e);return a}}();var bH,bI,bJ,bK=/alpha\([^)]*\)/i,bL=/opacity=([^)]*)/,bM=/^(top|right|bottom|left)$/,bN=/^(none|table(?!-c[ea]).+)/,bO=/^margin/,bP=new RegExp("^("+q+")(.*)$","i"),bQ=new RegExp("^("+q+")(?!px)[a-z%]+$","i"),bR=new RegExp("^([-+])=("+q+")","i"),bS={},bT={position:"absolute",visibility:"hidden",display:"block"},bU={letterSpacing:0,fontWeight:400},bV=["Top","Right","Bottom","Left"],bW=["Webkit","O","Moz","ms"],bX=p.fn.toggle;p.fn.extend({css:function(a,c){return p.access(this,function(a,c,d){return d!==b?p.style(a,c,d):p.css(a,c)},a,c,arguments.length>1)},show:function(){return b$(this,!0)},hide:function(){return b$(this)},toggle:function(a,b){var c=typeof a=="boolean";return p.isFunction(a)&&p.isFunction(b)?bX.apply(this,arguments):this.each(function(){(c?a:bZ(this))?p(this).show():p(this).hide()})}}),p.extend({cssHooks:{opacity:{get:function(a,b){if(b){var c=bH(a,"opacity");return c===""?"1":c}}}},cssNumber:{fillOpacity:!0,fontWeight:!0,lineHeight:!0,opacity:!0,orphans:!0,widows:!0,zIndex:!0,zoom:!0},cssProps:{"float":p.support.cssFloat?"cssFloat":"styleFloat"},style:function(a,c,d,e){if(!a||a.nodeType===3||a.nodeType===8||!a.style)return;var f,g,h,i=p.camelCase(c),j=a.style;c=p.cssProps[i]||(p.cssProps[i]=bY(j,i)),h=p.cssHooks[c]||p.cssHooks[i];if(d===b)return h&&"get"in h&&(f=h.get(a,!1,e))!==b?f:j[c];g=typeof d,g==="string"&&(f=bR.exec(d))&&(d=(f[1]+1)*f[2]+parseFloat(p.css(a,c)),g="number");if(d==null||g==="number"&&isNaN(d))return;g==="number"&&!p.cssNumber[i]&&(d+="px");if(!h||!("set"in h)||(d=h.set(a,d,e))!==b)try{j[c]=d}catch(k){}},css:function(a,c,d,e){var f,g,h,i=p.camelCase(c);return c=p.cssProps[i]||(p.cssProps[i]=bY(a.style,i)),h=p.cssHooks[c]||p.cssHooks[i],h&&"get"in h&&(f=h.get(a,!0,e)),f===b&&(f=bH(a,c)),f==="normal"&&c in bU&&(f=bU[c]),d||e!==b?(g=parseFloat(f),d||p.isNumeric(g)?g||0:f):f},swap:function(a,b,c){var d,e,f={};for(e in b)f[e]=a.style[e],a.style[e]=b[e];d=c.call(a);for(e in b)a.style[e]=f[e];return d}}),a.getComputedStyle?bH=function(b,c){var d,e,f,g,h=a.getComputedStyle(b,null),i=b.style;return h&&(d=h[c],d===""&&!p.contains(b.ownerDocument,b)&&(d=p.style(b,c)),bQ.test(d)&&bO.test(c)&&(e=i.width,f=i.minWidth,g=i.maxWidth,i.minWidth=i.maxWidth=i.width=d,d=h.width,i.width=e,i.minWidth=f,i.maxWidth=g)),d}:e.documentElement.currentStyle&&(bH=function(a,b){var c,d,e=a.currentStyle&&a.currentStyle[b],f=a.style;return e==null&&f&&f[b]&&(e=f[b]),bQ.test(e)&&!bM.test(b)&&(c=f.left,d=a.runtimeStyle&&a.runtimeStyle.left,d&&(a.runtimeStyle.left=a.currentStyle.left),f.left=b==="fontSize"?"1em":e,e=f.pixelLeft+"px",f.left=c,d&&(a.runtimeStyle.left=d)),e===""?"auto":e}),p.each(["height","width"],function(a,b){p.cssHooks[b]={get:function(a,c,d){if(c)return a.offsetWidth===0&&bN.test(bH(a,"display"))?p.swap(a,bT,function(){return cb(a,b,d)}):cb(a,b,d)},set:function(a,c,d){return b_(a,c,d?ca(a,b,d,p.support.boxSizing&&p.css(a,"boxSizing")==="border-box"):0)}}}),p.support.opacity||(p.cssHooks.opacity={get:function(a,b){return bL.test((b&&a.currentStyle?a.currentStyle.filter:a.style.filter)||"")?.01*parseFloat(RegExp.$1)+"":b?"1":""},set:function(a,b){var c=a.style,d=a.currentStyle,e=p.isNumeric(b)?"alpha(opacity="+b*100+")":"",f=d&&d.filter||c.filter||"";c.zoom=1;if(b>=1&&p.trim(f.replace(bK,""))===""&&c.removeAttribute){c.removeAttribute("filter");if(d&&!d.filter)return}c.filter=bK.test(f)?f.replace(bK,e):f+" "+e}}),p(function(){p.support.reliableMarginRight||(p.cssHooks.marginRight={get:function(a,b){return p.swap(a,{display:"inline-block"},function(){if(b)return bH(a,"marginRight")})}}),!p.support.pixelPosition&&p.fn.position&&p.each(["top","left"],function(a,b){p.cssHooks[b]={get:function(a,c){if(c){var d=bH(a,b);return bQ.test(d)?p(a).position()[b]+"px":d}}}})}),p.expr&&p.expr.filters&&(p.expr.filters.hidden=function(a){return a.offsetWidth===0&&a.offsetHeight===0||!p.support.reliableHiddenOffsets&&(a.style&&a.style.display||bH(a,"display"))==="none"},p.expr.filters.visible=function(a){return!p.expr.filters.hidden(a)}),p.each({margin:"",padding:"",border:"Width"},function(a,b){p.cssHooks[a+b]={expand:function(c){var d,e=typeof c=="string"?c.split(" "):[c],f={};for(d=0;d<4;d++)f[a+bV[d]+b]=e[d]||e[d-2]||e[0];return f}},bO.test(a)||(p.cssHooks[a+b].set=b_)});var cd=/%20/g,ce=/\[\]$/,cf=/\r?\n/g,cg=/^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,ch=/^(?:select|textarea)/i;p.fn.extend({serialize:function(){return p.param(this.serializeArray())},serializeArray:function(){return this.map(function(){return this.elements?p.makeArray(this.elements):this}).filter(function(){return this.name&&!this.disabled&&(this.checked||ch.test(this.nodeName)||cg.test(this.type))}).map(function(a,b){var c=p(this).val();return c==null?null:p.isArray(c)?p.map(c,function(a,c){return{name:b.name,value:a.replace(cf,"\r\n")}}):{name:b.name,value:c.replace(cf,"\r\n")}}).get()}}),p.param=function(a,c){var d,e=[],f=function(a,b){b=p.isFunction(b)?b():b==null?"":b,e[e.length]=encodeURIComponent(a)+"="+encodeURIComponent(b)};c===b&&(c=p.ajaxSettings&&p.ajaxSettings.traditional);if(p.isArray(a)||a.jquery&&!p.isPlainObject(a))p.each(a,function(){f(this.name,this.value)});else for(d in a)ci(d,a[d],c,f);return e.join("&").replace(cd,"+")};var cj,ck,cl=/#.*$/,cm=/^(.*?):[ \t]*([^\r\n]*)\r?$/mg,cn=/^(?:about|app|app\-storage|.+\-extension|file|res|widget):$/,co=/^(?:GET|HEAD)$/,cp=/^\/\//,cq=/\?/,cr=/<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi,cs=/([?&])_=[^&]*/,ct=/^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+)|)|)/,cu=p.fn.load,cv={},cw={},cx=["*/"]+["*"];try{cj=f.href}catch(cy){cj=e.createElement("a"),cj.href="",cj=cj.href}ck=ct.exec(cj.toLowerCase())||[],p.fn.load=function(a,c,d){if(typeof a!="string"&&cu)return cu.apply(this,arguments);if(!this.length)return this;var e,f,g,h=this,i=a.indexOf(" ");return i>=0&&(e=a.slice(i,a.length),a=a.slice(0,i)),p.isFunction(c)?(d=c,c=b):c&&typeof c=="object"&&(f="POST"),p.ajax({url:a,type:f,dataType:"html",data:c,complete:function(a,b){d&&h.each(d,g||[a.responseText,b,a])}}).done(function(a){g=arguments,h.html(e?p("<div>").append(a.replace(cr,"")).find(e):a)}),this},p.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "),function(a,b){p.fn[b]=function(a){return this.on(b,a)}}),p.each(["get","post"],function(a,c){p[c]=function(a,d,e,f){return p.isFunction(d)&&(f=f||e,e=d,d=b),p.ajax({type:c,url:a,data:d,success:e,dataType:f})}}),p.extend({getScript:function(a,c){return p.get(a,b,c,"script")},getJSON:function(a,b,c){return p.get(a,b,c,"json")},ajaxSetup:function(a,b){return b?cB(a,p.ajaxSettings):(b=a,a=p.ajaxSettings),cB(a,b),a},ajaxSettings:{url:cj,isLocal:cn.test(ck[1]),global:!0,type:"GET",contentType:"application/x-www-form-urlencoded; charset=UTF-8",processData:!0,async:!0,accepts:{xml:"application/xml, text/xml",html:"text/html",text:"text/plain",json:"application/json, text/javascript","*":cx},contents:{xml:/xml/,html:/html/,json:/json/},responseFields:{xml:"responseXML",text:"responseText"},converters:{"* text":a.String,"text html":!0,"text json":p.parseJSON,"text xml":p.parseXML},flatOptions:{context:!0,url:!0}},ajaxPrefilter:cz(cv),ajaxTransport:cz(cw),ajax:function(a,c){function y(a,c,f,i){var k,s,t,u,w,y=c;if(v===2)return;v=2,h&&clearTimeout(h),g=b,e=i||"",x.readyState=a>0?4:0,f&&(u=cC(l,x,f));if(a>=200&&a<300||a===304)l.ifModified&&(w=x.getResponseHeader("Last-Modified"),w&&(p.lastModified[d]=w),w=x.getResponseHeader("Etag"),w&&(p.etag[d]=w)),a===304?(y="notmodified",k=!0):(k=cD(l,u),y=k.state,s=k.data,t=k.error,k=!t);else{t=y;if(!y||a)y="error",a<0&&(a=0)}x.status=a,x.statusText=""+(c||y),k?o.resolveWith(m,[s,y,x]):o.rejectWith(m,[x,y,t]),x.statusCode(r),r=b,j&&n.trigger("ajax"+(k?"Success":"Error"),[x,l,k?s:t]),q.fireWith(m,[x,y]),j&&(n.trigger("ajaxComplete",[x,l]),--p.active||p.event.trigger("ajaxStop"))}typeof a=="object"&&(c=a,a=b),c=c||{};var d,e,f,g,h,i,j,k,l=p.ajaxSetup({},c),m=l.context||l,n=m!==l&&(m.nodeType||m instanceof p)?p(m):p.event,o=p.Deferred(),q=p.Callbacks("once memory"),r=l.statusCode||{},t={},u={},v=0,w="canceled",x={readyState:0,setRequestHeader:function(a,b){if(!v){var c=a.toLowerCase();a=u[c]=u[c]||a,t[a]=b}return this},getAllResponseHeaders:function(){return v===2?e:null},getResponseHeader:function(a){var c;if(v===2){if(!f){f={};while(c=cm.exec(e))f[c[1].toLowerCase()]=c[2]}c=f[a.toLowerCase()]}return c===b?null:c},overrideMimeType:function(a){return v||(l.mimeType=a),this},abort:function(a){return a=a||w,g&&g.abort(a),y(0,a),this}};o.promise(x),x.success=x.done,x.error=x.fail,x.complete=q.add,x.statusCode=function(a){if(a){var b;if(v<2)for(b in a)r[b]=[r[b],a[b]];else b=a[x.status],x.always(b)}return this},l.url=((a||l.url)+"").replace(cl,"").replace(cp,ck[1]+"//"),l.dataTypes=p.trim(l.dataType||"*").toLowerCase().split(s),l.crossDomain==null&&(i=ct.exec(l.url.toLowerCase()),l.crossDomain=!(!i||i[1]==ck[1]&&i[2]==ck[2]&&(i[3]||(i[1]==="http:"?80:443))==(ck[3]||(ck[1]==="http:"?80:443)))),l.data&&l.processData&&typeof l.data!="string"&&(l.data=p.param(l.data,l.traditional)),cA(cv,l,c,x);if(v===2)return x;j=l.global,l.type=l.type.toUpperCase(),l.hasContent=!co.test(l.type),j&&p.active++===0&&p.event.trigger("ajaxStart");if(!l.hasContent){l.data&&(l.url+=(cq.test(l.url)?"&":"?")+l.data,delete l.data),d=l.url;if(l.cache===!1){var z=p.now(),A=l.url.replace(cs,"$1_="+z);l.url=A+(A===l.url?(cq.test(l.url)?"&":"?")+"_="+z:"")}}(l.data&&l.hasContent&&l.contentType!==!1||c.contentType)&&x.setRequestHeader("Content-Type",l.contentType),l.ifModified&&(d=d||l.url,p.lastModified[d]&&x.setRequestHeader("If-Modified-Since",p.lastModified[d]),p.etag[d]&&x.setRequestHeader("If-None-Match",p.etag[d])),x.setRequestHeader("Accept",l.dataTypes[0]&&l.accepts[l.dataTypes[0]]?l.accepts[l.dataTypes[0]]+(l.dataTypes[0]!=="*"?", "+cx+"; q=0.01":""):l.accepts["*"]);for(k in l.headers)x.setRequestHeader(k,l.headers[k]);if(!l.beforeSend||l.beforeSend.call(m,x,l)!==!1&&v!==2){w="abort";for(k in{success:1,error:1,complete:1})x[k](l[k]);g=cA(cw,l,c,x);if(!g)y(-1,"No Transport");else{x.readyState=1,j&&n.trigger("ajaxSend",[x,l]),l.async&&l.timeout>0&&(h=setTimeout(function(){x.abort("timeout")},l.timeout));try{v=1,g.send(t,y)}catch(B){if(v<2)y(-1,B);else throw B}}return x}return x.abort()},active:0,lastModified:{},etag:{}});var cE=[],cF=/\?/,cG=/(=)\?(?=&|$)|\?\?/,cH=p.now();p.ajaxSetup({jsonp:"callback",jsonpCallback:function(){var a=cE.pop()||p.expando+"_"+cH++;return this[a]=!0,a}}),p.ajaxPrefilter("json jsonp",function(c,d,e){var f,g,h,i=c.data,j=c.url,k=c.jsonp!==!1,l=k&&cG.test(j),m=k&&!l&&typeof i=="string"&&!(c.contentType||"").indexOf("application/x-www-form-urlencoded")&&cG.test(i);if(c.dataTypes[0]==="jsonp"||l||m)return f=c.jsonpCallback=p.isFunction(c.jsonpCallback)?c.jsonpCallback():c.jsonpCallback,g=a[f],l?c.url=j.replace(cG,"$1"+f):m?c.data=i.replace(cG,"$1"+f):k&&(c.url+=(cF.test(j)?"&":"?")+c.jsonp+"="+f),c.converters["script json"]=function(){return h||p.error(f+" was not called"),h[0]},c.dataTypes[0]="json",a[f]=function(){h=arguments},e.always(function(){a[f]=g,c[f]&&(c.jsonpCallback=d.jsonpCallback,cE.push(f)),h&&p.isFunction(g)&&g(h[0]),h=g=b}),"script"}),p.ajaxSetup({accepts:{script:"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},contents:{script:/javascript|ecmascript/},converters:{"text script":function(a){return p.globalEval(a),a}}}),p.ajaxPrefilter("script",function(a){a.cache===b&&(a.cache=!1),a.crossDomain&&(a.type="GET",a.global=!1)}),p.ajaxTransport("script",function(a){if(a.crossDomain){var c,d=e.head||e.getElementsByTagName("head")[0]||e.documentElement;return{send:function(f,g){c=e.createElement("script"),c.async="async",a.scriptCharset&&(c.charset=a.scriptCharset),c.src=a.url,c.onload=c.onreadystatechange=function(a,e){if(e||!c.readyState||/loaded|complete/.test(c.readyState))c.onload=c.onreadystatechange=null,d&&c.parentNode&&d.removeChild(c),c=b,e||g(200,"success")},d.insertBefore(c,d.firstChild)},abort:function(){c&&c.onload(0,1)}}}});var cI,cJ=a.ActiveXObject?function(){for(var a in cI)cI[a](0,1)}:!1,cK=0;p.ajaxSettings.xhr=a.ActiveXObject?function(){return!this.isLocal&&cL()||cM()}:cL,function(a){p.extend(p.support,{ajax:!!a,cors:!!a&&"withCredentials"in a})}(p.ajaxSettings.xhr()),p.support.ajax&&p.ajaxTransport(function(c){if(!c.crossDomain||p.support.cors){var d;return{send:function(e,f){var g,h,i=c.xhr();c.username?i.open(c.type,c.url,c.async,c.username,c.password):i.open(c.type,c.url,c.async);if(c.xhrFields)for(h in c.xhrFields)i[h]=c.xhrFields[h];c.mimeType&&i.overrideMimeType&&i.overrideMimeType(c.mimeType),!c.crossDomain&&!e["X-Requested-With"]&&(e["X-Requested-With"]="XMLHttpRequest");try{for(h in e)i.setRequestHeader(h,e[h])}catch(j){}i.send(c.hasContent&&c.data||null),d=function(a,e){var h,j,k,l,m;try{if(d&&(e||i.readyState===4)){d=b,g&&(i.onreadystatechange=p.noop,cJ&&delete cI[g]);if(e)i.readyState!==4&&i.abort();else{h=i.status,k=i.getAllResponseHeaders(),l={},m=i.responseXML,m&&m.documentElement&&(l.xml=m);try{l.text=i.responseText}catch(a){}try{j=i.statusText}catch(n){j=""}!h&&c.isLocal&&!c.crossDomain?h=l.text?200:404:h===1223&&(h=204)}}}catch(o){e||f(-1,o)}l&&f(h,j,l,k)},c.async?i.readyState===4?setTimeout(d,0):(g=++cK,cJ&&(cI||(cI={},p(a).unload(cJ)),cI[g]=d),i.onreadystatechange=d):d()},abort:function(){d&&d(0,1)}}}});var cN,cO,cP=/^(?:toggle|show|hide)$/,cQ=new RegExp("^(?:([-+])=|)("+q+")([a-z%]*)$","i"),cR=/queueHooks$/,cS=[cY],cT={"*":[function(a,b){var c,d,e,f=this.createTween(a,b),g=cQ.exec(b),h=f.cur(),i=+h||0,j=1;if(g){c=+g[2],d=g[3]||(p.cssNumber[a]?"":"px");if(d!=="px"&&i){i=p.css(f.elem,a,!0)||c||1;do e=j=j||".5",i=i/j,p.style(f.elem,a,i+d),j=f.cur()/h;while(j!==1&&j!==e)}f.unit=d,f.start=i,f.end=g[1]?i+(g[1]+1)*c:c}return f}]};p.Animation=p.extend(cW,{tweener:function(a,b){p.isFunction(a)?(b=a,a=["*"]):a=a.split(" ");var c,d=0,e=a.length;for(;d<e;d++)c=a[d],cT[c]=cT[c]||[],cT[c].unshift(b)},prefilter:function(a,b){b?cS.unshift(a):cS.push(a)}}),p.Tween=cZ,cZ.prototype={constructor:cZ,init:function(a,b,c,d,e,f){this.elem=a,this.prop=c,this.easing=e||"swing",this.options=b,this.start=this.now=this.cur(),this.end=d,this.unit=f||(p.cssNumber[c]?"":"px")},cur:function(){var a=cZ.propHooks[this.prop];return a&&a.get?a.get(this):cZ.propHooks._default.get(this)},run:function(a){var b,c=cZ.propHooks[this.prop];return this.options.duration?this.pos=b=p.easing[this.easing](a,this.options.duration*a,0,1,this.options.duration):this.pos=b=a,this.now=(this.end-this.start)*b+this.start,this.options.step&&this.options.step.call(this.elem,this.now,this),c&&c.set?c.set(this):cZ.propHooks._default.set(this),this}},cZ.prototype.init.prototype=cZ.prototype,cZ.propHooks={_default:{get:function(a){var b;return a.elem[a.prop]==null||!!a.elem.style&&a.elem.style[a.prop]!=null?(b=p.css(a.elem,a.prop,!1,""),!b||b==="auto"?0:b):a.elem[a.prop]},set:function(a){p.fx.step[a.prop]?p.fx.step[a.prop](a):a.elem.style&&(a.elem.style[p.cssProps[a.prop]]!=null||p.cssHooks[a.prop])?p.style(a.elem,a.prop,a.now+a.unit):a.elem[a.prop]=a.now}}},cZ.propHooks.scrollTop=cZ.propHooks.scrollLeft={set:function(a){a.elem.nodeType&&a.elem.parentNode&&(a.elem[a.prop]=a.now)}},p.each(["toggle","show","hide"],function(a,b){var c=p.fn[b];p.fn[b]=function(d,e,f){return d==null||typeof d=="boolean"||!a&&p.isFunction(d)&&p.isFunction(e)?c.apply(this,arguments):this.animate(c$(b,!0),d,e,f)}}),p.fn.extend({fadeTo:function(a,b,c,d){return this.filter(bZ).css("opacity",0).show().end().animate({opacity:b},a,c,d)},animate:function(a,b,c,d){var e=p.isEmptyObject(a),f=p.speed(b,c,d),g=function(){var b=cW(this,p.extend({},a),f);e&&b.stop(!0)};return e||f.queue===!1?this.each(g):this.queue(f.queue,g)},stop:function(a,c,d){var e=function(a){var b=a.stop;delete a.stop,b(d)};return typeof a!="string"&&(d=c,c=a,a=b),c&&a!==!1&&this.queue(a||"fx",[]),this.each(function(){var b=!0,c=a!=null&&a+"queueHooks",f=p.timers,g=p._data(this);if(c)g[c]&&g[c].stop&&e(g[c]);else for(c in g)g[c]&&g[c].stop&&cR.test(c)&&e(g[c]);for(c=f.length;c--;)f[c].elem===this&&(a==null||f[c].queue===a)&&(f[c].anim.stop(d),b=!1,f.splice(c,1));(b||!d)&&p.dequeue(this,a)})}}),p.each({slideDown:c$("show"),slideUp:c$("hide"),slideToggle:c$("toggle"),fadeIn:{opacity:"show"},fadeOut:{opacity:"hide"},fadeToggle:{opacity:"toggle"}},function(a,b){p.fn[a]=function(a,c,d){return this.animate(b,a,c,d)}}),p.speed=function(a,b,c){var d=a&&typeof a=="object"?p.extend({},a):{complete:c||!c&&b||p.isFunction(a)&&a,duration:a,easing:c&&b||b&&!p.isFunction(b)&&b};d.duration=p.fx.off?0:typeof d.duration=="number"?d.duration:d.duration in p.fx.speeds?p.fx.speeds[d.duration]:p.fx.speeds._default;if(d.queue==null||d.queue===!0)d.queue="fx";return d.old=d.complete,d.complete=function(){p.isFunction(d.old)&&d.old.call(this),d.queue&&p.dequeue(this,d.queue)},d},p.easing={linear:function(a){return a},swing:function(a){return.5-Math.cos(a*Math.PI)/2}},p.timers=[],p.fx=cZ.prototype.init,p.fx.tick=function(){var a,b=p.timers,c=0;for(;c<b.length;c++)a=b[c],!a()&&b[c]===a&&b.splice(c--,1);b.length||p.fx.stop()},p.fx.timer=function(a){a()&&p.timers.push(a)&&!cO&&(cO=setInterval(p.fx.tick,p.fx.interval))},p.fx.interval=13,p.fx.stop=function(){clearInterval(cO),cO=null},p.fx.speeds={slow:600,fast:200,_default:400},p.fx.step={},p.expr&&p.expr.filters&&(p.expr.filters.animated=function(a){return p.grep(p.timers,function(b){return a===b.elem}).length});var c_=/^(?:body|html)$/i;p.fn.offset=function(a){if(arguments.length)return a===b?this:this.each(function(b){p.offset.setOffset(this,a,b)});var c,d,e,f,g,h,i,j,k,l,m=this[0],n=m&&m.ownerDocument;if(!n)return;return(e=n.body)===m?p.offset.bodyOffset(m):(d=n.documentElement,p.contains(d,m)?(c=m.getBoundingClientRect(),f=da(n),g=d.clientTop||e.clientTop||0,h=d.clientLeft||e.clientLeft||0,i=f.pageYOffset||d.scrollTop,j=f.pageXOffset||d.scrollLeft,k=c.top+i-g,l=c.left+j-h,{top:k,left:l}):{top:0,left:0})},p.offset={bodyOffset:function(a){var b=a.offsetTop,c=a.offsetLeft;return p.support.doesNotIncludeMarginInBodyOffset&&(b+=parseFloat(p.css(a,"marginTop"))||0,c+=parseFloat(p.css(a,"marginLeft"))||0),{top:b,left:c}},setOffset:function(a,b,c){var d=p.css(a,"position");d==="static"&&(a.style.position="relative");var e=p(a),f=e.offset(),g=p.css(a,"top"),h=p.css(a,"left"),i=(d==="absolute"||d==="fixed")&&p.inArray("auto",[g,h])>-1,j={},k={},l,m;i?(k=e.position(),l=k.top,m=k.left):(l=parseFloat(g)||0,m=parseFloat(h)||0),p.isFunction(b)&&(b=b.call(a,c,f)),b.top!=null&&(j.top=b.top-f.top+l),b.left!=null&&(j.left=b.left-f.left+m),"using"in b?b.using.call(a,j):e.css(j)}},p.fn.extend({position:function(){if(!this[0])return;var a=this[0],b=this.offsetParent(),c=this.offset(),d=c_.test(b[0].nodeName)?{top:0,left:0}:b.offset();return c.top-=parseFloat(p.css(a,"marginTop"))||0,c.left-=parseFloat(p.css(a,"marginLeft"))||0,d.top+=parseFloat(p.css(b[0],"borderTopWidth"))||0,d.left+=parseFloat(p.css(b[0],"borderLeftWidth"))||0,{top:c.top-d.top,left:c.left-d.left}},offsetParent:function(){return this.map(function(){var a=this.offsetParent||e.body;while(a&&!c_.test(a.nodeName)&&p.css(a,"position")==="static")a=a.offsetParent;return a||e.body})}}),p.each({scrollLeft:"pageXOffset",scrollTop:"pageYOffset"},function(a,c){var d=/Y/.test(c);p.fn[a]=function(e){return p.access(this,function(a,e,f){var g=da(a);if(f===b)return g?c in g?g[c]:g.document.documentElement[e]:a[e];g?g.scrollTo(d?p(g).scrollLeft():f,d?f:p(g).scrollTop()):a[e]=f},a,e,arguments.length,null)}}),p.each({Height:"height",Width:"width"},function(a,c){p.each({padding:"inner"+a,content:c,"":"outer"+a},function(d,e){p.fn[e]=function(e,f){var g=arguments.length&&(d||typeof e!="boolean"),h=d||(e===!0||f===!0?"margin":"border");return p.access(this,function(c,d,e){var f;return p.isWindow(c)?c.document.documentElement["client"+a]:c.nodeType===9?(f=c.documentElement,Math.max(c.body["scroll"+a],f["scroll"+a],c.body["offset"+a],f["offset"+a],f["client"+a])):e===b?p.css(c,d,e,h):p.style(c,d,e,h)},c,g?e:b,g,null)}})}),a.jQuery=a.$=p,typeof define=="function"&&define.amd&&define.amd.jQuery&&define("jquery",[],function(){return p})})(window);/*! jQuery UI - v1.8.23 - 2012-08-15
* https://github.com/jquery/jquery-ui
* Includes: jquery.ui.core.js
* Copyright (c) 2012 AUTHORS.txt; Licensed MIT, GPL */
(function(a,b){function c(b,c){var e=b.nodeName.toLowerCase();if("area"===e){var f=b.parentNode,g=f.name,h;return!b.href||!g||f.nodeName.toLowerCase()!=="map"?!1:(h=a("img[usemap=#"+g+"]")[0],!!h&&d(h))}return(/input|select|textarea|button|object/.test(e)?!b.disabled:"a"==e?b.href||c:c)&&d(b)}function d(b){return!a(b).parents().andSelf().filter(function(){return a.curCSS(this,"visibility")==="hidden"||a.expr.filters.hidden(this)}).length}a.ui=a.ui||{};if(a.ui.version)return;a.extend(a.ui,{version:"1.8.23",keyCode:{ALT:18,BACKSPACE:8,CAPS_LOCK:20,COMMA:188,COMMAND:91,COMMAND_LEFT:91,COMMAND_RIGHT:93,CONTROL:17,DELETE:46,DOWN:40,END:35,ENTER:13,ESCAPE:27,HOME:36,INSERT:45,LEFT:37,MENU:93,NUMPAD_ADD:107,NUMPAD_DECIMAL:110,NUMPAD_DIVIDE:111,NUMPAD_ENTER:108,NUMPAD_MULTIPLY:106,NUMPAD_SUBTRACT:109,PAGE_DOWN:34,PAGE_UP:33,PERIOD:190,RIGHT:39,SHIFT:16,SPACE:32,TAB:9,UP:38,WINDOWS:91}}),a.fn.extend({propAttr:a.fn.prop||a.fn.attr,_focus:a.fn.focus,focus:function(b,c){return typeof b=="number"?this.each(function(){var d=this;setTimeout(function(){a(d).focus(),c&&c.call(d)},b)}):this._focus.apply(this,arguments)},scrollParent:function(){var b;return a.browser.msie&&/(static|relative)/.test(this.css("position"))||/absolute/.test(this.css("position"))?b=this.parents().filter(function(){return/(relative|absolute|fixed)/.test(a.curCSS(this,"position",1))&&/(auto|scroll)/.test(a.curCSS(this,"overflow",1)+a.curCSS(this,"overflow-y",1)+a.curCSS(this,"overflow-x",1))}).eq(0):b=this.parents().filter(function(){return/(auto|scroll)/.test(a.curCSS(this,"overflow",1)+a.curCSS(this,"overflow-y",1)+a.curCSS(this,"overflow-x",1))}).eq(0),/fixed/.test(this.css("position"))||!b.length?a(document):b},zIndex:function(c){if(c!==b)return this.css("zIndex",c);if(this.length){var d=a(this[0]),e,f;while(d.length&&d[0]!==document){e=d.css("position");if(e==="absolute"||e==="relative"||e==="fixed"){f=parseInt(d.css("zIndex"),10);if(!isNaN(f)&&f!==0)return f}d=d.parent()}}return 0},disableSelection:function(){return this.bind((a.support.selectstart?"selectstart":"mousedown")+".ui-disableSelection",function(a){a.preventDefault()})},enableSelection:function(){return this.unbind(".ui-disableSelection")}}),a("<a>").outerWidth(1).jquery||a.each(["Width","Height"],function(c,d){function h(b,c,d,f){return a.each(e,function(){c-=parseFloat(a.curCSS(b,"padding"+this,!0))||0,d&&(c-=parseFloat(a.curCSS(b,"border"+this+"Width",!0))||0),f&&(c-=parseFloat(a.curCSS(b,"margin"+this,!0))||0)}),c}var e=d==="Width"?["Left","Right"]:["Top","Bottom"],f=d.toLowerCase(),g={innerWidth:a.fn.innerWidth,innerHeight:a.fn.innerHeight,outerWidth:a.fn.outerWidth,outerHeight:a.fn.outerHeight};a.fn["inner"+d]=function(c){return c===b?g["inner"+d].call(this):this.each(function(){a(this).css(f,h(this,c)+"px")})},a.fn["outer"+d]=function(b,c){return typeof b!="number"?g["outer"+d].call(this,b):this.each(function(){a(this).css(f,h(this,b,!0,c)+"px")})}}),a.extend(a.expr[":"],{data:a.expr.createPseudo?a.expr.createPseudo(function(b){return function(c){return!!a.data(c,b)}}):function(b,c,d){return!!a.data(b,d[3])},focusable:function(b){return c(b,!isNaN(a.attr(b,"tabindex")))},tabbable:function(b){var d=a.attr(b,"tabindex"),e=isNaN(d);return(e||d>=0)&&c(b,!e)}}),a(function(){var b=document.body,c=b.appendChild(c=document.createElement("div"));c.offsetHeight,a.extend(c.style,{minHeight:"100px",height:"auto",padding:0,borderWidth:0}),a.support.minHeight=c.offsetHeight===100,a.support.selectstart="onselectstart"in c,b.removeChild(c).style.display="none"}),a.curCSS||(a.curCSS=a.css),a.extend(a.ui,{plugin:{add:function(b,c,d){var e=a.ui[b].prototype;for(var f in d)e.plugins[f]=e.plugins[f]||[],e.plugins[f].push([c,d[f]])},call:function(a,b,c){var d=a.plugins[b];if(!d||!a.element[0].parentNode)return;for(var e=0;e<d.length;e++)a.options[d[e][0]]&&d[e][1].apply(a.element,c)}},contains:function(a,b){return document.compareDocumentPosition?a.compareDocumentPosition(b)&16:a!==b&&a.contains(b)},hasScroll:function(b,c){if(a(b).css("overflow")==="hidden")return!1;var d=c&&c==="left"?"scrollLeft":"scrollTop",e=!1;return b[d]>0?!0:(b[d]=1,e=b[d]>0,b[d]=0,e)},isOverAxis:function(a,b,c){return a>b&&a<b+c},isOver:function(b,c,d,e,f,g){return a.ui.isOverAxis(b,d,f)&&a.ui.isOverAxis(c,e,g)}})})(jQuery);;/*! jQuery UI - v1.8.23 - 2012-08-15
* https://github.com/jquery/jquery-ui
* Includes: jquery.ui.widget.js
* Copyright (c) 2012 AUTHORS.txt; Licensed MIT, GPL */
(function(a,b){if(a.cleanData){var c=a.cleanData;a.cleanData=function(b){for(var d=0,e;(e=b[d])!=null;d++)try{a(e).triggerHandler("remove")}catch(f){}c(b)}}else{var d=a.fn.remove;a.fn.remove=function(b,c){return this.each(function(){return c||(!b||a.filter(b,[this]).length)&&a("*",this).add([this]).each(function(){try{a(this).triggerHandler("remove")}catch(b){}}),d.call(a(this),b,c)})}}a.widget=function(b,c,d){var e=b.split(".")[0],f;b=b.split(".")[1],f=e+"-"+b,d||(d=c,c=a.Widget),a.expr[":"][f]=function(c){return!!a.data(c,b)},a[e]=a[e]||{},a[e][b]=function(a,b){arguments.length&&this._createWidget(a,b)};var g=new c;g.options=a.extend(!0,{},g.options),a[e][b].prototype=a.extend(!0,g,{namespace:e,widgetName:b,widgetEventPrefix:a[e][b].prototype.widgetEventPrefix||b,widgetBaseClass:f},d),a.widget.bridge(b,a[e][b])},a.widget.bridge=function(c,d){a.fn[c]=function(e){var f=typeof e=="string",g=Array.prototype.slice.call(arguments,1),h=this;return e=!f&&g.length?a.extend.apply(null,[!0,e].concat(g)):e,f&&e.charAt(0)==="_"?h:(f?this.each(function(){var d=a.data(this,c),f=d&&a.isFunction(d[e])?d[e].apply(d,g):d;if(f!==d&&f!==b)return h=f,!1}):this.each(function(){var b=a.data(this,c);b?b.option(e||{})._init():a.data(this,c,new d(e,this))}),h)}},a.Widget=function(a,b){arguments.length&&this._createWidget(a,b)},a.Widget.prototype={widgetName:"widget",widgetEventPrefix:"",options:{disabled:!1},_createWidget:function(b,c){a.data(c,this.widgetName,this),this.element=a(c),this.options=a.extend(!0,{},this.options,this._getCreateOptions(),b);var d=this;this.element.bind("remove."+this.widgetName,function(){d.destroy()}),this._create(),this._trigger("create"),this._init()},_getCreateOptions:function(){return a.metadata&&a.metadata.get(this.element[0])[this.widgetName]},_create:function(){},_init:function(){},destroy:function(){this.element.unbind("."+this.widgetName).removeData(this.widgetName),this.widget().unbind("."+this.widgetName).removeAttr("aria-disabled").removeClass(this.widgetBaseClass+"-disabled "+"ui-state-disabled")},widget:function(){return this.element},option:function(c,d){var e=c;if(arguments.length===0)return a.extend({},this.options);if(typeof c=="string"){if(d===b)return this.options[c];e={},e[c]=d}return this._setOptions(e),this},_setOptions:function(b){var c=this;return a.each(b,function(a,b){c._setOption(a,b)}),this},_setOption:function(a,b){return this.options[a]=b,a==="disabled"&&this.widget()[b?"addClass":"removeClass"](this.widgetBaseClass+"-disabled"+" "+"ui-state-disabled").attr("aria-disabled",b),this},enable:function(){return this._setOption("disabled",!1)},disable:function(){return this._setOption("disabled",!0)},_trigger:function(b,c,d){var e,f,g=this.options[b];d=d||{},c=a.Event(c),c.type=(b===this.widgetEventPrefix?b:this.widgetEventPrefix+b).toLowerCase(),c.target=this.element[0],f=c.originalEvent;if(f)for(e in f)e in c||(c[e]=f[e]);return this.element.trigger(c,d),!(a.isFunction(g)&&g.call(this.element[0],c,d)===!1||c.isDefaultPrevented())}}})(jQuery);;/*! jQuery UI - v1.8.23 - 2012-08-15
* https://github.com/jquery/jquery-ui
* Includes: jquery.ui.position.js
* Copyright (c) 2012 AUTHORS.txt; Licensed MIT, GPL */
(function(a,b){a.ui=a.ui||{};var c=/left|center|right/,d=/top|center|bottom/,e="center",f={},g=a.fn.position,h=a.fn.offset;a.fn.position=function(b){if(!b||!b.of)return g.apply(this,arguments);b=a.extend({},b);var h=a(b.of),i=h[0],j=(b.collision||"flip").split(" "),k=b.offset?b.offset.split(" "):[0,0],l,m,n;return i.nodeType===9?(l=h.width(),m=h.height(),n={top:0,left:0}):i.setTimeout?(l=h.width(),m=h.height(),n={top:h.scrollTop(),left:h.scrollLeft()}):i.preventDefault?(b.at="left top",l=m=0,n={top:b.of.pageY,left:b.of.pageX}):(l=h.outerWidth(),m=h.outerHeight(),n=h.offset()),a.each(["my","at"],function(){var a=(b[this]||"").split(" ");a.length===1&&(a=c.test(a[0])?a.concat([e]):d.test(a[0])?[e].concat(a):[e,e]),a[0]=c.test(a[0])?a[0]:e,a[1]=d.test(a[1])?a[1]:e,b[this]=a}),j.length===1&&(j[1]=j[0]),k[0]=parseInt(k[0],10)||0,k.length===1&&(k[1]=k[0]),k[1]=parseInt(k[1],10)||0,b.at[0]==="right"?n.left+=l:b.at[0]===e&&(n.left+=l/2),b.at[1]==="bottom"?n.top+=m:b.at[1]===e&&(n.top+=m/2),n.left+=k[0],n.top+=k[1],this.each(function(){var c=a(this),d=c.outerWidth(),g=c.outerHeight(),h=parseInt(a.curCSS(this,"marginLeft",!0))||0,i=parseInt(a.curCSS(this,"marginTop",!0))||0,o=d+h+(parseInt(a.curCSS(this,"marginRight",!0))||0),p=g+i+(parseInt(a.curCSS(this,"marginBottom",!0))||0),q=a.extend({},n),r;b.my[0]==="right"?q.left-=d:b.my[0]===e&&(q.left-=d/2),b.my[1]==="bottom"?q.top-=g:b.my[1]===e&&(q.top-=g/2),f.fractions||(q.left=Math.round(q.left),q.top=Math.round(q.top)),r={left:q.left-h,top:q.top-i},a.each(["left","top"],function(c,e){a.ui.position[j[c]]&&a.ui.position[j[c]][e](q,{targetWidth:l,targetHeight:m,elemWidth:d,elemHeight:g,collisionPosition:r,collisionWidth:o,collisionHeight:p,offset:k,my:b.my,at:b.at})}),a.fn.bgiframe&&c.bgiframe(),c.offset(a.extend(q,{using:b.using}))})},a.ui.position={fit:{left:function(b,c){var d=a(window),e=c.collisionPosition.left+c.collisionWidth-d.width()-d.scrollLeft();b.left=e>0?b.left-e:Math.max(b.left-c.collisionPosition.left,b.left)},top:function(b,c){var d=a(window),e=c.collisionPosition.top+c.collisionHeight-d.height()-d.scrollTop();b.top=e>0?b.top-e:Math.max(b.top-c.collisionPosition.top,b.top)}},flip:{left:function(b,c){if(c.at[0]===e)return;var d=a(window),f=c.collisionPosition.left+c.collisionWidth-d.width()-d.scrollLeft(),g=c.my[0]==="left"?-c.elemWidth:c.my[0]==="right"?c.elemWidth:0,h=c.at[0]==="left"?c.targetWidth:-c.targetWidth,i=-2*c.offset[0];b.left+=c.collisionPosition.left<0?g+h+i:f>0?g+h+i:0},top:function(b,c){if(c.at[1]===e)return;var d=a(window),f=c.collisionPosition.top+c.collisionHeight-d.height()-d.scrollTop(),g=c.my[1]==="top"?-c.elemHeight:c.my[1]==="bottom"?c.elemHeight:0,h=c.at[1]==="top"?c.targetHeight:-c.targetHeight,i=-2*c.offset[1];b.top+=c.collisionPosition.top<0?g+h+i:f>0?g+h+i:0}}},a.offset.setOffset||(a.offset.setOffset=function(b,c){/static/.test(a.curCSS(b,"position"))&&(b.style.position="relative");var d=a(b),e=d.offset(),f=parseInt(a.curCSS(b,"top",!0),10)||0,g=parseInt(a.curCSS(b,"left",!0),10)||0,h={top:c.top-e.top+f,left:c.left-e.left+g};"using"in c?c.using.call(b,h):d.css(h)},a.fn.offset=function(b){var c=this[0];return!c||!c.ownerDocument?null:b?a.isFunction(b)?this.each(function(c){a(this).offset(b.call(this,c,a(this).offset()))}):this.each(function(){a.offset.setOffset(this,b)}):h.call(this)}),a.curCSS||(a.curCSS=a.css),function(){var b=document.getElementsByTagName("body")[0],c=document.createElement("div"),d,e,g,h,i;d=document.createElement(b?"div":"body"),g={visibility:"hidden",width:0,height:0,border:0,margin:0,background:"none"},b&&a.extend(g,{position:"absolute",left:"-1000px",top:"-1000px"});for(var j in g)d.style[j]=g[j];d.appendChild(c),e=b||document.documentElement,e.insertBefore(d,e.firstChild),c.style.cssText="position: absolute; left: 10.7432222px; top: 10.432325px; height: 30px; width: 201px;",h=a(c).offset(function(a,b){return b}).offset(),d.innerHTML="",e.removeChild(d),i=h.top+h.left+(b?2e3:0),f.fractions=i>21&&i<22}()})(jQuery);;/*! jQuery UI - v1.8.23 - 2012-08-15
* https://github.com/jquery/jquery-ui
* Includes: jquery.ui.dialog.js
* Copyright (c) 2012 AUTHORS.txt; Licensed MIT, GPL */
(function(a,b){var c="ui-dialog ui-widget ui-widget-content ui-corner-all ",d={buttons:!0,height:!0,maxHeight:!0,maxWidth:!0,minHeight:!0,minWidth:!0,width:!0},e={maxHeight:!0,maxWidth:!0,minHeight:!0,minWidth:!0};a.widget("ui.dialog",{options:{autoOpen:!0,buttons:{},closeOnEscape:!0,closeText:"close",dialogClass:"",draggable:!0,hide:null,height:"auto",maxHeight:!1,maxWidth:!1,minHeight:150,minWidth:150,modal:!1,position:{my:"center",at:"center",collision:"fit",using:function(b){var c=a(this).css(b).offset().top;c<0&&a(this).css("top",b.top-c)}},resizable:!0,show:null,stack:!0,title:"",width:300,zIndex:1e3},_create:function(){this.originalTitle=this.element.attr("title"),typeof this.originalTitle!="string"&&(this.originalTitle=""),this.options.title=this.options.title||this.originalTitle;var b=this,d=b.options,e=d.title||"&#160;",f=a.ui.dialog.getTitleId(b.element),g=(b.uiDialog=a("<div></div>")).appendTo(document.body).hide().addClass(c+d.dialogClass).css({zIndex:d.zIndex}).attr("tabIndex",-1).css("outline",0).keydown(function(c){d.closeOnEscape&&!c.isDefaultPrevented()&&c.keyCode&&c.keyCode===a.ui.keyCode.ESCAPE&&(b.close(c),c.preventDefault())}).attr({role:"dialog","aria-labelledby":f}).mousedown(function(a){b.moveToTop(!1,a)}),h=b.element.show().removeAttr("title").addClass("ui-dialog-content ui-widget-content").appendTo(g),i=(b.uiDialogTitlebar=a("<div></div>")).addClass("ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix").prependTo(g),j=a('<a href="#"></a>').addClass("ui-dialog-titlebar-close ui-corner-all").attr("role","button").hover(function(){j.addClass("ui-state-hover")},function(){j.removeClass("ui-state-hover")}).focus(function(){j.addClass("ui-state-focus")}).blur(function(){j.removeClass("ui-state-focus")}).click(function(a){return b.close(a),!1}).appendTo(i),k=(b.uiDialogTitlebarCloseText=a("<span></span>")).addClass("ui-icon ui-icon-closethick").text(d.closeText).appendTo(j),l=a("<span></span>").addClass("ui-dialog-title").attr("id",f).html(e).prependTo(i);a.isFunction(d.beforeclose)&&!a.isFunction(d.beforeClose)&&(d.beforeClose=d.beforeclose),i.find("*").add(i).disableSelection(),d.draggable&&a.fn.draggable&&b._makeDraggable(),d.resizable&&a.fn.resizable&&b._makeResizable(),b._createButtons(d.buttons),b._isOpen=!1,a.fn.bgiframe&&g.bgiframe()},_init:function(){this.options.autoOpen&&this.open()},destroy:function(){var a=this;return a.overlay&&a.overlay.destroy(),a.uiDialog.hide(),a.element.unbind(".dialog").removeData("dialog").removeClass("ui-dialog-content ui-widget-content").hide().appendTo("body"),a.uiDialog.remove(),a.originalTitle&&a.element.attr("title",a.originalTitle),a},widget:function(){return this.uiDialog},close:function(b){var c=this,d,e;if(!1===c._trigger("beforeClose",b))return;return c.overlay&&c.overlay.destroy(),c.uiDialog.unbind("keypress.ui-dialog"),c._isOpen=!1,c.options.hide?c.uiDialog.hide(c.options.hide,function(){c._trigger("close",b)}):(c.uiDialog.hide(),c._trigger("close",b)),a.ui.dialog.overlay.resize(),c.options.modal&&(d=0,a(".ui-dialog").each(function(){this!==c.uiDialog[0]&&(e=a(this).css("z-index"),isNaN(e)||(d=Math.max(d,e)))}),a.ui.dialog.maxZ=d),c},isOpen:function(){return this._isOpen},moveToTop:function(b,c){var d=this,e=d.options,f;return e.modal&&!b||!e.stack&&!e.modal?d._trigger("focus",c):(e.zIndex>a.ui.dialog.maxZ&&(a.ui.dialog.maxZ=e.zIndex),d.overlay&&(a.ui.dialog.maxZ+=1,d.overlay.$el.css("z-index",a.ui.dialog.overlay.maxZ=a.ui.dialog.maxZ)),f={scrollTop:d.element.scrollTop(),scrollLeft:d.element.scrollLeft()},a.ui.dialog.maxZ+=1,d.uiDialog.css("z-index",a.ui.dialog.maxZ),d.element.attr(f),d._trigger("focus",c),d)},open:function(){if(this._isOpen)return;var b=this,c=b.options,d=b.uiDialog;return b.overlay=c.modal?new a.ui.dialog.overlay(b):null,b._size(),b._position(c.position),d.show(c.show),b.moveToTop(!0),c.modal&&d.bind("keydown.ui-dialog",function(b){if(b.keyCode!==a.ui.keyCode.TAB)return;var c=a(":tabbable",this),d=c.filter(":first"),e=c.filter(":last");if(b.target===e[0]&&!b.shiftKey)return d.focus(1),!1;if(b.target===d[0]&&b.shiftKey)return e.focus(1),!1}),a(b.element.find(":tabbable").get().concat(d.find(".ui-dialog-buttonpane :tabbable").get().concat(d.get()))).eq(0).focus(),b._isOpen=!0,b._trigger("open"),b},_createButtons:function(b){var c=this,d=!1,e=a("<div></div>").addClass("ui-dialog-buttonpane ui-widget-content ui-helper-clearfix"),f=a("<div></div>").addClass("ui-dialog-buttonset").appendTo(e);c.uiDialog.find(".ui-dialog-buttonpane").remove(),typeof b=="object"&&b!==null&&a.each(b,function(){return!(d=!0)}),d&&(a.each(b,function(b,d){d=a.isFunction(d)?{click:d,text:b}:d;var e=a('<button type="button"></button>').click(function(){d.click.apply(c.element[0],arguments)}).appendTo(f);a.each(d,function(a,b){if(a==="click")return;a in e?e[a](b):e.attr(a,b)}),a.fn.button&&e.button()}),e.appendTo(c.uiDialog))},_makeDraggable:function(){function f(a){return{position:a.position,offset:a.offset}}var b=this,c=b.options,d=a(document),e;b.uiDialog.draggable({cancel:".ui-dialog-content, .ui-dialog-titlebar-close",handle:".ui-dialog-titlebar",containment:"document",start:function(d,g){e=c.height==="auto"?"auto":a(this).height(),a(this).height(a(this).height()).addClass("ui-dialog-dragging"),b._trigger("dragStart",d,f(g))},drag:function(a,c){b._trigger("drag",a,f(c))},stop:function(g,h){c.position=[h.position.left-d.scrollLeft(),h.position.top-d.scrollTop()],a(this).removeClass("ui-dialog-dragging").height(e),b._trigger("dragStop",g,f(h)),a.ui.dialog.overlay.resize()}})},_makeResizable:function(c){function h(a){return{originalPosition:a.originalPosition,originalSize:a.originalSize,position:a.position,size:a.size}}c=c===b?this.options.resizable:c;var d=this,e=d.options,f=d.uiDialog.css("position"),g=typeof c=="string"?c:"n,e,s,w,se,sw,ne,nw";d.uiDialog.resizable({cancel:".ui-dialog-content",containment:"document",alsoResize:d.element,maxWidth:e.maxWidth,maxHeight:e.maxHeight,minWidth:e.minWidth,minHeight:d._minHeight(),handles:g,start:function(b,c){a(this).addClass("ui-dialog-resizing"),d._trigger("resizeStart",b,h(c))},resize:function(a,b){d._trigger("resize",a,h(b))},stop:function(b,c){a(this).removeClass("ui-dialog-resizing"),e.height=a(this).height(),e.width=a(this).width(),d._trigger("resizeStop",b,h(c)),a.ui.dialog.overlay.resize()}}).css("position",f).find(".ui-resizable-se").addClass("ui-icon ui-icon-grip-diagonal-se")},_minHeight:function(){var a=this.options;return a.height==="auto"?a.minHeight:Math.min(a.minHeight,a.height)},_position:function(b){var c=[],d=[0,0],e;if(b){if(typeof b=="string"||typeof b=="object"&&"0"in b)c=b.split?b.split(" "):[b[0],b[1]],c.length===1&&(c[1]=c[0]),a.each(["left","top"],function(a,b){+c[a]===c[a]&&(d[a]=c[a],c[a]=b)}),b={my:c.join(" "),at:c.join(" "),offset:d.join(" ")};b=a.extend({},a.ui.dialog.prototype.options.position,b)}else b=a.ui.dialog.prototype.options.position;e=this.uiDialog.is(":visible"),e||this.uiDialog.show(),this.uiDialog.css({top:0,left:0}).position(a.extend({of:window},b)),e||this.uiDialog.hide()},_setOptions:function(b){var c=this,f={},g=!1;a.each(b,function(a,b){c._setOption(a,b),a in d&&(g=!0),a in e&&(f[a]=b)}),g&&this._size(),this.uiDialog.is(":data(resizable)")&&this.uiDialog.resizable("option",f)},_setOption:function(b,d){var e=this,f=e.uiDialog;switch(b){case"beforeclose":b="beforeClose";break;case"buttons":e._createButtons(d);break;case"closeText":e.uiDialogTitlebarCloseText.text(""+d);break;case"dialogClass":f.removeClass(e.options.dialogClass).addClass(c+d);break;case"disabled":d?f.addClass("ui-dialog-disabled"):f.removeClass("ui-dialog-disabled");break;case"draggable":var g=f.is(":data(draggable)");g&&!d&&f.draggable("destroy"),!g&&d&&e._makeDraggable();break;case"position":e._position(d);break;case"resizable":var h=f.is(":data(resizable)");h&&!d&&f.resizable("destroy"),h&&typeof d=="string"&&f.resizable("option","handles",d),!h&&d!==!1&&e._makeResizable(d);break;case"title":a(".ui-dialog-title",e.uiDialogTitlebar).html(""+(d||"&#160;"))}a.Widget.prototype._setOption.apply(e,arguments)},_size:function(){var b=this.options,c,d,e=this.uiDialog.is(":visible");this.element.show().css({width:"auto",minHeight:0,height:0}),b.minWidth>b.width&&(b.width=b.minWidth),c=this.uiDialog.css({height:"auto",width:b.width}).height(),d=Math.max(0,b.minHeight-c);if(b.height==="auto")if(a.support.minHeight)this.element.css({minHeight:d,height:"auto"});else{this.uiDialog.show();var f=this.element.css("height","auto").height();e||this.uiDialog.hide(),this.element.height(Math.max(f,d))}else this.element.height(Math.max(b.height-c,0));this.uiDialog.is(":data(resizable)")&&this.uiDialog.resizable("option","minHeight",this._minHeight())}}),a.extend(a.ui.dialog,{version:"1.8.23",uuid:0,maxZ:0,getTitleId:function(a){var b=a.attr("id");return b||(this.uuid+=1,b=this.uuid),"ui-dialog-title-"+b},overlay:function(b){this.$el=a.ui.dialog.overlay.create(b)}}),a.extend(a.ui.dialog.overlay,{instances:[],oldInstances:[],maxZ:0,events:a.map("focus,mousedown,mouseup,keydown,keypress,click".split(","),function(a){return a+".dialog-overlay"}).join(" "),create:function(b){this.instances.length===0&&(setTimeout(function(){a.ui.dialog.overlay.instances.length&&a(document).bind(a.ui.dialog.overlay.events,function(b){if(a(b.target).zIndex()<a.ui.dialog.overlay.maxZ)return!1})},1),a(document).bind("keydown.dialog-overlay",function(c){b.options.closeOnEscape&&!c.isDefaultPrevented()&&c.keyCode&&c.keyCode===a.ui.keyCode.ESCAPE&&(b.close(c),c.preventDefault())}),a(window).bind("resize.dialog-overlay",a.ui.dialog.overlay.resize));var c=(this.oldInstances.pop()||a("<div></div>").addClass("ui-widget-overlay")).appendTo(document.body).css({width:this.width(),height:this.height()});return a.fn.bgiframe&&c.bgiframe(),this.instances.push(c),c},destroy:function(b){var c=a.inArray(b,this.instances);c!=-1&&this.oldInstances.push(this.instances.splice(c,1)[0]),this.instances.length===0&&a([document,window]).unbind(".dialog-overlay"),b.remove();var d=0;a.each(this.instances,function(){d=Math.max(d,this.css("z-index"))}),this.maxZ=d},height:function(){var b,c;return a.browser.msie&&a.browser.version<7?(b=Math.max(document.documentElement.scrollHeight,document.body.scrollHeight),c=Math.max(document.documentElement.offsetHeight,document.body.offsetHeight),b<c?a(window).height()+"px":b+"px"):a(document).height()+"px"},width:function(){var b,c;return a.browser.msie?(b=Math.max(document.documentElement.scrollWidth,document.body.scrollWidth),c=Math.max(document.documentElement.offsetWidth,document.body.offsetWidth),b<c?a(window).width()+"px":b+"px"):a(document).width()+"px"},resize:function(){var b=a([]);a.each(a.ui.dialog.overlay.instances,function(){b=b.add(this)}),b.css({width:0,height:0}).css({width:a.ui.dialog.overlay.width(),height:a.ui.dialog.overlay.height()})}}),a.extend(a.ui.dialog.overlay.prototype,{destroy:function(){a.ui.dialog.overlay.destroy(this.$el)}})})(jQuery);;/*
Copyright 2012 Igor Vaynberg
 
Version: 3.2 Timestamp: Mon Sep 10 10:38:04 PDT 2012

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in
compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is
distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
*/
(function(e){"undefined"==typeof e.fn.each2&&e.fn.extend({each2:function(g){for(var i=e([0]),m=-1,s=this.length;++m<s&&(i.context=i[0]=this[m])&&!1!==g.call(i[0],m,i););return this}})})(jQuery);
(function(e,g){function i(a,b){var c=0,d=b.length,j;if("undefined"===typeof a)return-1;if(a.constructor===String)for(;c<d;c+=1){if(0===a.localeCompare(b[c]))return c}else for(;c<d;c+=1)if(j=b[c],j.constructor===String){if(0===j.localeCompare(a))return c}else if(j===a)return c;return-1}function m(a,b){return a===b?!0:a===g||b===g||null===a||null===b?!1:a.constructor===String?0===a.localeCompare(b):b.constructor===String?0===b.localeCompare(a):!1}function s(a,b){var c,d,j;if(null===a||1>a.length)return[];
c=a.split(b);d=0;for(j=c.length;d<j;d+=1)c[d]=e.trim(c[d]);return c}function A(a,b,c){var c=c||g,d;return function(){var j=arguments;window.clearTimeout(d);d=window.setTimeout(function(){b.apply(c,j)},a)}}function l(a){a.preventDefault();a.stopPropagation()}function B(a,b,c){var d=a.toUpperCase().indexOf(b.toUpperCase()),b=b.length;0>d?c.push(a):(c.push(a.substring(0,d)),c.push("<span class='select2-match'>"),c.push(a.substring(d,d+b)),c.push("</span>"),c.push(a.substring(d+b,a.length)))}function C(a){var b,
c=0,d=null,j=a.quietMillis||100;return function(h){window.clearTimeout(b);b=window.setTimeout(function(){var b=c+=1,j=a.data,n=a.transport||e.ajax,f=a.traditional||!1,g=a.type||"GET",j=j.call(this,h.term,h.page,h.context);null!==d&&d.abort();d=n.call(null,{url:a.url,dataType:a.dataType,data:j,type:g,traditional:f,success:function(d){b<c||(d=a.results(d,h.page),h.callback(d))}})},j)}}function D(a){var b=a,c,d=function(a){return""+a.text};e.isArray(b)||(d=b.text,e.isFunction(d)||(c=b.text,d=function(a){return a[c]}),
b=b.results);return function(a){var c=a.term,f={results:[]},k;if(c==="")a.callback({results:b});else{k=function(b,f){var g,t,b=b[0];if(b.children){g={};for(t in b)b.hasOwnProperty(t)&&(g[t]=b[t]);g.children=[];e(b.children).each2(function(a,b){k(b,g.children)});g.children.length&&f.push(g)}else a.matcher(c,d(b))&&f.push(b)};e(b).each2(function(a,b){k(b,f.results)});a.callback(f)}}}function E(a){return e.isFunction(a)?a:function(b){var c=b.term,d={results:[]};e(a).each(function(){var a=this.text!==
g,e=a?this.text:this;if(""===c||b.matcher(c,e))d.results.push(a?this:{id:this,text:this})});b.callback(d)}}function u(a){if(e.isFunction(a))return!0;if(!a)return!1;throw Error("formatterName must be a function or a falsy value");}function v(a){return e.isFunction(a)?a():a}function F(a){var b=0;e.each(a,function(a,d){d.children?b+=F(d.children):b++});return b}function H(a,b,c,d){var e=a,h=!1,f,k,n,o;if(!d.createSearchChoice||!d.tokenSeparators||1>d.tokenSeparators.length)return g;for(;;){h=-1;k=0;
for(n=d.tokenSeparators.length;k<n&&!(o=d.tokenSeparators[k],h=a.indexOf(o),0<=h);k++);if(0>h)break;f=a.substring(0,h);a=a.substring(h+o.length);if(0<f.length&&(f=d.createSearchChoice(f,b),f!==g&&null!==f&&d.id(f)!==g&&null!==d.id(f))){h=!1;k=0;for(n=b.length;k<n;k++)if(m(d.id(f),d.id(b[k]))){h=!0;break}h||c(f)}}if(0!=e.localeCompare(a))return a}function x(a,b){var c=function(){};c.prototype=new a;c.prototype.constructor=c;c.prototype.parent=a.prototype;c.prototype=e.extend(c.prototype,b);return c}
if(window.Select2===g){var f,w,y,z,G,q;f={TAB:9,ENTER:13,ESC:27,SPACE:32,LEFT:37,UP:38,RIGHT:39,DOWN:40,SHIFT:16,CTRL:17,ALT:18,PAGE_UP:33,PAGE_DOWN:34,HOME:36,END:35,BACKSPACE:8,DELETE:46,isArrow:function(a){a=a.which?a.which:a;switch(a){case f.LEFT:case f.RIGHT:case f.UP:case f.DOWN:return!0}return!1},isControl:function(a){switch(a.which){case f.SHIFT:case f.CTRL:case f.ALT:return!0}return a.metaKey?!0:!1},isFunctionKey:function(a){a=a.which?a.which:a;return 112<=a&&123>=a}};var I=1;G=function(){return I++};
e(document).delegate("body","mousemove",function(a){e.data(document,"select2-lastpos",{x:a.pageX,y:a.pageY})});e(document).ready(function(){e(document).delegate("body","mousedown touchend",function(a){var b=e(a.target).closest("div.select2-container").get(0),c;b?e(document).find("div.select2-container-active").each(function(){this!==b&&e(this).data("select2").blur()}):(b=e(a.target).closest("div.select2-drop").get(0),e(document).find("div.select2-drop-active").each(function(){this!==b&&e(this).data("select2").blur()}));
b=e(a.target);c=b.attr("for");"LABEL"===a.target.tagName&&(c&&0<c.length)&&(b=e("#"+c),b=b.data("select2"),b!==g&&(b.focus(),a.preventDefault()))})});w=x(Object,{bind:function(a){var b=this;return function(){a.apply(b,arguments)}},init:function(a){var b,c;this.opts=a=this.prepareOpts(a);this.id=a.id;a.element.data("select2")!==g&&null!==a.element.data("select2")&&this.destroy();this.enabled=!0;this.container=this.createContainer();this.containerId="s2id_"+(a.element.attr("id")||"autogen"+G());this.containerSelector=
"#"+this.containerId.replace(/([;&,\.\+\*\~':"\!\^#$%@\[\]\(\)=>\|])/g,"\\$1");this.container.attr("id",this.containerId);var d=!1,j;this.body=function(){!1===d&&(j=a.element.closest("body"),d=!0);return j};a.element.attr("class")!==g&&this.container.addClass(a.element.attr("class").replace(/validate\[[\S ]+] ?/,""));this.container.css(v(a.containerCss));this.container.addClass(v(a.containerCssClass));this.opts.element.data("select2",this).hide().before(this.container);this.container.data("select2",
this);this.dropdown=this.container.find(".select2-drop");this.dropdown.addClass(v(a.dropdownCssClass));this.dropdown.data("select2",this);this.results=b=this.container.find(".select2-results");this.search=c=this.container.find("input.select2-input");c.attr("tabIndex",this.opts.element.attr("tabIndex"));this.resultsPage=0;this.context=null;this.initContainer();this.initContainerWidth();this.results.bind("mousemove",function(a){var b=e.data(document,"select2-lastpos");(b===g||b.x!==a.pageX||b.y!==a.pageY)&&
e(a.target).trigger("mousemove-filtered",a)});this.dropdown.delegate(".select2-results","mousemove-filtered",this.bind(this.highlightUnderEvent));var h=this.results,f=A(80,function(a){h.trigger("scroll-debounced",a)});h.bind("scroll",function(a){0<=i(a.target,h.get())&&f(a)});this.dropdown.delegate(".select2-results","scroll-debounced",this.bind(this.loadMoreIfNeeded));e.fn.mousewheel&&b.mousewheel(function(a,c,d,e){c=b.scrollTop();0<e&&0>=c-e?(b.scrollTop(0),l(a)):0>e&&b.get(0).scrollHeight-b.scrollTop()+
e<=b.height()&&(b.scrollTop(b.get(0).scrollHeight-b.height()),l(a))});c.bind("keydown",function(){e.data(c,"keyup-change-value")===g&&e.data(c,"keyup-change-value",c.val())});c.bind("keyup",function(){var a=e.data(c,"keyup-change-value");a!==g&&c.val()!==a&&(e.removeData(c,"keyup-change-value"),c.trigger("keyup-change"))});c.bind("keyup-change",this.bind(this.updateResults));c.bind("focus",function(){c.addClass("select2-focused");" "===c.val()&&c.val("")});c.bind("blur",function(){c.removeClass("select2-focused")});
this.dropdown.delegate(".select2-results","mouseup",this.bind(function(a){0<e(a.target).closest(".select2-result-selectable:not(.select2-disabled)").length?(this.highlightUnderEvent(a),this.selectHighlighted(a)):this.focusSearch();l(a)}));this.dropdown.bind("click mouseup mousedown",function(a){a.stopPropagation()});e.isFunction(this.opts.initSelection)&&(this.initSelection(),this.monitorSource());(a.element.is(":disabled")||a.element.is("[readonly='readonly']"))&&this.disable()},destroy:function(){var a=
this.opts.element.data("select2");a!==g&&(a.container.remove(),a.dropdown.remove(),a.opts.element.removeData("select2").unbind(".select2").show())},prepareOpts:function(a){var b,c,d;b=a.element;"select"===b.get(0).tagName.toLowerCase()&&(this.select=c=a.element);c&&e.each("id multiple ajax query createSearchChoice initSelection data tags".split(" "),function(){if(this in a)throw Error("Option '"+this+"' is not allowed for Select2 when attached to a <select> element.");});a=e.extend({},{populateResults:function(b,
c,d){var f,n=this.opts.id,o=this;f=function(b,c,j){var h,l,i,m,r,p,q;h=0;for(l=b.length;h<l;h=h+1){i=b[h];m=n(i)!==g;r=i.children&&i.children.length>0;p=e("<li></li>");p.addClass("select2-results-dept-"+j);p.addClass("select2-result");p.addClass(m?"select2-result-selectable":"select2-result-unselectable");r&&p.addClass("select2-result-with-children");p.addClass(o.opts.formatResultCssClass(i));m=e("<div></div>");m.addClass("select2-result-label");q=a.formatResult(i,m,d);q!==g&&m.html(o.opts.escapeMarkup(q));
p.append(m);if(r){r=e("<ul></ul>");r.addClass("select2-result-sub");f(i.children,r,j+1);p.append(r)}p.data("select2-data",i);c.append(p)}};f(c,b,0)}},e.fn.select2.defaults,a);"function"!==typeof a.id&&(d=a.id,a.id=function(a){return a[d]});if(c)a.query=this.bind(function(a){var c={results:[],more:false},d=a.term,f,n,o;o=function(b,c){var e;if(b.is("option"))a.matcher(d,b.text(),b)&&c.push({id:b.attr("value"),text:b.text(),element:b.get(),css:b.attr("class")});else if(b.is("optgroup")){e={text:b.attr("label"),
children:[],element:b.get(),css:b.attr("class")};b.children().each2(function(a,b){o(b,e.children)});e.children.length>0&&c.push(e)}};f=b.children();if(this.getPlaceholder()!==g&&f.length>0){n=f[0];e(n).text()===""&&(f=f.not(n))}f.each2(function(a,b){o(b,c.results)});a.callback(c)}),a.id=function(a){return a.id},a.formatResultCssClass=function(a){return a.css};else if(!("query"in a))if("ajax"in a){if((c=a.element.data("ajax-url"))&&0<c.length)a.ajax.url=c;a.query=C(a.ajax)}else"data"in a?a.query=D(a.data):
"tags"in a&&(a.query=E(a.tags),a.createSearchChoice=function(a){return{id:a,text:a}},a.initSelection=function(b,c){var d=[];e(s(b.val(),a.separator)).each(function(){var b=this,c=this,j=a.tags;e.isFunction(j)&&(j=j());e(j).each(function(){if(m(this.id,b)){c=this.text;return false}});d.push({id:b,text:c})});c(d)});if("function"!==typeof a.query)throw"query function not defined for Select2 "+a.element.attr("id");return a},monitorSource:function(){this.opts.element.bind("change.select2",this.bind(function(){!0!==
this.opts.element.data("select2-change-triggered")&&this.initSelection()}))},triggerChange:function(a){a=a||{};a=e.extend({},a,{type:"change",val:this.val()});this.opts.element.data("select2-change-triggered",!0);this.opts.element.trigger(a);this.opts.element.data("select2-change-triggered",!1);this.opts.element.click();this.opts.blurOnChange&&this.opts.element.blur()},enable:function(){this.enabled||(this.enabled=!0,this.container.removeClass("select2-container-disabled"))},disable:function(){this.enabled&&
(this.close(),this.enabled=!1,this.container.addClass("select2-container-disabled"))},opened:function(){return this.container.hasClass("select2-dropdown-open")},positionDropdown:function(){var a=this.container.offset(),b=this.container.outerHeight(),c=this.container.outerWidth(),d=this.dropdown.outerHeight(),j=e(window).scrollTop()+document.documentElement.clientHeight,b=a.top+b,f=a.left,j=b+d<=j,g=a.top-d>=this.body().scrollTop(),k=this.dropdown.hasClass("select2-drop-above"),n;"static"!==this.body().css("position")&&
(n=this.body().offset(),b-=n.top,f-=n.left);k?(k=!0,!g&&j&&(k=!1)):(k=!1,!j&&g&&(k=!0));k?(b=a.top-d,this.container.addClass("select2-drop-above"),this.dropdown.addClass("select2-drop-above")):(this.container.removeClass("select2-drop-above"),this.dropdown.removeClass("select2-drop-above"));a=e.extend({top:b,left:f,width:c},v(this.opts.dropdownCss));this.dropdown.css(a)},shouldOpen:function(){var a;if(this.opened())return!1;a=e.Event("open");this.opts.element.trigger(a);return!a.isDefaultPrevented()},
clearDropdownAlignmentPreference:function(){this.container.removeClass("select2-drop-above");this.dropdown.removeClass("select2-drop-above")},open:function(){if(!this.shouldOpen())return!1;window.setTimeout(this.bind(this.opening),1);return!0},opening:function(){var a=this.containerId,b=this.containerSelector,c="scroll."+a,d="resize."+a;this.container.parents().each(function(){e(this).bind(c,function(){var a=e(b);0==a.length&&e(this).unbind(c);a.select2("close")})});e(window).bind(d,function(){var a=
e(b);0==a.length&&e(window).unbind(d);a.select2("close")});this.clearDropdownAlignmentPreference();" "===this.search.val()&&this.search.val("");this.container.addClass("select2-dropdown-open").addClass("select2-container-active");this.updateResults(!0);this.dropdown[0]!==this.body().children().last()[0]&&this.dropdown.detach().appendTo(this.body());this.dropdown.show();this.positionDropdown();this.dropdown.addClass("select2-drop-active");this.ensureHighlightVisible();this.focusSearch()},close:function(){if(this.opened()){var a=
this;this.container.parents().each(function(){e(this).unbind("scroll."+a.containerId)});e(window).unbind("resize."+this.containerId);this.clearDropdownAlignmentPreference();this.dropdown.hide();this.container.removeClass("select2-dropdown-open").removeClass("select2-container-active");this.results.empty();this.clearSearch();this.opts.element.trigger(e.Event("close"))}},clearSearch:function(){},ensureHighlightVisible:function(){var a=this.results,b,c,d,f;c=this.highlight();0>c||(0==c?a.scrollTop(0):
(b=a.find(".select2-result-selectable"),d=e(b[c]),f=d.offset().top+d.outerHeight(),c===b.length-1&&(b=a.find("li.select2-more-results"),0<b.length&&(f=b.offset().top+b.outerHeight())),b=a.offset().top+a.outerHeight(),f>b&&a.scrollTop(a.scrollTop()+(f-b)),d=d.offset().top-a.offset().top,0>d&&a.scrollTop(a.scrollTop()+d)))},moveHighlight:function(a){for(var b=this.results.find(".select2-result-selectable"),c=this.highlight();-1<c&&c<b.length;){var c=c+a,d=e(b[c]);if(d.hasClass("select2-result-selectable")&&
!d.hasClass("select2-disabled")){this.highlight(c);break}}},highlight:function(a){var b=this.results.find(".select2-result-selectable").not(".select2-disabled");if(0===arguments.length)return i(b.filter(".select2-highlighted")[0],b.get());a>=b.length&&(a=b.length-1);0>a&&(a=0);b.removeClass("select2-highlighted");e(b[a]).addClass("select2-highlighted");this.ensureHighlightVisible()},countSelectableResults:function(){return this.results.find(".select2-result-selectable").not(".select2-disabled").length},
highlightUnderEvent:function(a){a=e(a.target).closest(".select2-result-selectable");if(0<a.length&&!a.is(".select2-highlighted")){var b=this.results.find(".select2-result-selectable");this.highlight(b.index(a))}else 0==a.length&&this.results.find(".select2-highlighted").removeClass("select2-highlighted")},loadMoreIfNeeded:function(){var a=this.results,b=a.find("li.select2-more-results"),c,d=this.resultsPage+1,e=this,f=this.search.val(),g=this.context;0!==b.length&&(c=b.offset().top-a.offset().top-
a.height(),0>=c&&(b.addClass("select2-active"),this.opts.query({term:f,page:d,context:g,matcher:this.opts.matcher,callback:this.bind(function(c){e.opened()&&(e.opts.populateResults.call(this,a,c.results,{term:f,page:d,context:g}),!0===c.more?(b.detach().appendTo(a).text(e.opts.formatLoadMore(d+1)),window.setTimeout(function(){e.loadMoreIfNeeded()},10)):b.remove(),e.positionDropdown(),e.resultsPage=d)})})))},tokenize:function(){},updateResults:function(a){function b(){f.scrollTop(0);d.removeClass("select2-active");
k.positionDropdown()}function c(a){f.html(k.opts.escapeMarkup(a));b()}var d=this.search,f=this.results,h=this.opts,i,k=this;if(!(!0!==a&&(!1===this.showSearchInput||!this.opened()))){d.addClass("select2-active");if(1<=h.maximumSelectionSize&&(i=this.data(),e.isArray(i)&&i.length>=h.maximumSelectionSize&&u(h.formatSelectionTooBig,"formatSelectionTooBig"))){c("<li class='select2-selection-limit'>"+h.formatSelectionTooBig(h.maximumSelectionSize)+"</li>");return}d.val().length<h.minimumInputLength&&u(h.formatInputTooShort,
"formatInputTooShort")?c("<li class='select2-no-results'>"+h.formatInputTooShort(d.val(),h.minimumInputLength)+"</li>"):(c("<li class='select2-searching'>"+h.formatSearching()+"</li>"),i=this.tokenize(),i!=g&&null!=i&&d.val(i),this.resultsPage=1,h.query({term:d.val(),page:this.resultsPage,context:null,matcher:h.matcher,callback:this.bind(function(i){var l;this.opened()&&((this.context=i.context===g?null:i.context,this.opts.createSearchChoice&&""!==d.val()&&(l=this.opts.createSearchChoice.call(null,
d.val(),i.results),l!==g&&null!==l&&k.id(l)!==g&&null!==k.id(l)&&0===e(i.results).filter(function(){return m(k.id(this),k.id(l))}).length&&i.results.unshift(l)),0===i.results.length&&u(h.formatNoMatches,"formatNoMatches"))?c("<li class='select2-no-results'>"+h.formatNoMatches(d.val())+"</li>"):(f.empty(),k.opts.populateResults.call(this,f,i.results,{term:d.val(),page:this.resultsPage,context:null}),!0===i.more&&u(h.formatLoadMore,"formatLoadMore")&&(f.append("<li class='select2-more-results'>"+k.opts.escapeMarkup(h.formatLoadMore(this.resultsPage))+
"</li>"),window.setTimeout(function(){k.loadMoreIfNeeded()},10)),this.postprocessResults(i,a),b()))})}))}},cancel:function(){this.close()},blur:function(){this.close();this.container.removeClass("select2-container-active");this.dropdown.removeClass("select2-drop-active");this.search[0]===document.activeElement&&this.search.blur();this.clearSearch();this.selection.find(".select2-search-choice-focus").removeClass("select2-search-choice-focus")},focusSearch:function(){this.search.show();this.search.focus();
window.setTimeout(this.bind(function(){this.search.show();this.search.focus();this.search.val(this.search.val())}),10)},selectHighlighted:function(){var a=this.highlight(),b=this.results.find(".select2-highlighted").not(".select2-disabled"),c=b.closest(".select2-result-selectable").data("select2-data");c&&(b.addClass("select2-disabled"),this.highlight(a),this.onSelect(c))},getPlaceholder:function(){return this.opts.element.attr("placeholder")||this.opts.element.attr("data-placeholder")||this.opts.element.data("placeholder")||
this.opts.placeholder},initContainerWidth:function(){var a=function(){var a,c,d,f;if("off"===this.opts.width)return null;if("element"===this.opts.width)return 0===this.opts.element.outerWidth()?"auto":this.opts.element.outerWidth()+"px";if("copy"===this.opts.width||"resolve"===this.opts.width){a=this.opts.element.attr("style");if(a!==g){a=a.split(";");d=0;for(f=a.length;d<f;d+=1)if(c=a[d].replace(/\s/g,"").match(/width:(([-+]?([0-9]*\.)?[0-9]+)(px|em|ex|%|in|cm|mm|pt|pc))/),null!==c&&1<=c.length)return c[1]}return"resolve"===
this.opts.width?(a=this.opts.element.css("width"),0<a.indexOf("%")?a:0===this.opts.element.outerWidth()?"auto":this.opts.element.outerWidth()+"px"):null}return e.isFunction(this.opts.width)?this.opts.width():this.opts.width}.call(this);null!==a&&this.container.attr("style","width: "+a)}});y=x(w,{createContainer:function(){return e("<div></div>",{"class":"select2-container"}).html("    <a href='#' onclick='return false;' class='select2-choice'>   <span></span><abbr class='select2-search-choice-close' style='display:none;'></abbr>   <div><b></b></div></a>    <div class='select2-drop select2-offscreen'>   <div class='select2-search'>       <input type='text' autocomplete='off' class='select2-input'/>   </div>   <ul class='select2-results'>   </ul></div>")},
opening:function(){this.search.show();this.parent.opening.apply(this,arguments);this.dropdown.removeClass("select2-offscreen")},close:function(){this.opened()&&(this.parent.close.apply(this,arguments),this.dropdown.removeAttr("style").addClass("select2-offscreen").insertAfter(this.selection).show())},focus:function(){this.close();this.selection.focus()},isFocused:function(){return this.selection[0]===document.activeElement},cancel:function(){this.parent.cancel.apply(this,arguments);this.selection.focus()},
initContainer:function(){var a,b=this.dropdown;this.selection=a=this.container.find(".select2-choice");this.search.bind("keydown",this.bind(function(a){if(this.enabled)if(a.which===f.PAGE_UP||a.which===f.PAGE_DOWN)l(a);else if(this.opened())switch(a.which){case f.UP:case f.DOWN:this.moveHighlight(a.which===f.UP?-1:1);l(a);break;case f.TAB:case f.ENTER:this.selectHighlighted();l(a);break;case f.ESC:this.cancel(a),l(a)}else a.which===f.TAB||f.isControl(a)||f.isFunctionKey(a)||a.which===f.ESC||!1===
this.opts.openOnEnter&&a.which===f.ENTER||this.open()}));this.search.bind("focus",this.bind(function(){this.selection.attr("tabIndex","-1")}));this.search.bind("blur",this.bind(function(){this.opened()||this.container.removeClass("select2-container-active");window.setTimeout(this.bind(function(){this.selection.attr("tabIndex",this.opts.element.attr("tabIndex"))}),10)}));a.bind("mousedown",this.bind(function(){this.opened()?(this.close(),this.selection.focus()):this.enabled&&this.open()}));b.bind("mousedown",
this.bind(function(){this.search.focus()}));a.bind("focus",this.bind(function(){this.container.addClass("select2-container-active");this.search.attr("tabIndex","-1")}));a.bind("blur",this.bind(function(){this.opened()||this.container.removeClass("select2-container-active");window.setTimeout(this.bind(function(){this.search.attr("tabIndex",this.opts.element.attr("tabIndex"))}),10)}));a.bind("keydown",this.bind(function(a){if(this.enabled)if(a.which===f.PAGE_UP||a.which===f.PAGE_DOWN)l(a);else if(!(a.which===
f.TAB||f.isControl(a)||f.isFunctionKey(a)||a.which===f.ESC)&&!(!1===this.opts.openOnEnter&&a.which===f.ENTER))if(a.which==f.DELETE)this.opts.allowClear&&this.clear();else{this.open();if(a.which!==f.ENTER&&!(48>a.which)){var b=String.fromCharCode(a.which).toLowerCase();a.shiftKey&&(b=b.toUpperCase());this.search.focus();this.search.val(b)}l(a)}}));a.delegate("abbr","mousedown",this.bind(function(a){this.enabled&&(this.clear(),l(a),this.close(),this.triggerChange(),this.selection.focus())}));this.setPlaceholder();
this.search.bind("focus",this.bind(function(){this.container.addClass("select2-container-active")}))},clear:function(){this.opts.element.val("");this.selection.find("span").empty();this.selection.removeData("select2-data");this.setPlaceholder()},initSelection:function(){if(""===this.opts.element.val())this.close(),this.setPlaceholder();else{var a=this;this.opts.initSelection.call(null,this.opts.element,function(b){b!==g&&null!==b&&(a.updateSelection(b),a.close(),a.setPlaceholder())})}},prepareOpts:function(){var a=
this.parent.prepareOpts.apply(this,arguments);"select"===a.element.get(0).tagName.toLowerCase()&&(a.initSelection=function(a,c){var d=a.find(":selected");e.isFunction(c)&&c({id:d.attr("value"),text:d.text()})});return a},setPlaceholder:function(){var a=this.getPlaceholder();""===this.opts.element.val()&&a!==g&&!(this.select&&""!==this.select.find("option:first").text())&&(this.selection.find("span").html(this.opts.escapeMarkup(a)),this.selection.addClass("select2-default"),this.selection.find("abbr").hide())},
postprocessResults:function(a,b){var c=0,d=this,f=!0;this.results.find(".select2-result-selectable").each2(function(a,b){if(m(d.id(b.data("select2-data")),d.opts.element.val()))return c=a,!1});this.highlight(c);!0===b&&(f=this.showSearchInput=F(a.results)>=this.opts.minimumResultsForSearch,this.dropdown.find(".select2-search")[f?"removeClass":"addClass"]("select2-search-hidden"),e(this.dropdown,this.container)[f?"addClass":"removeClass"]("select2-with-searchbox"))},onSelect:function(a){var b=this.opts.element.val();
this.opts.element.val(this.id(a));this.updateSelection(a);this.close();this.selection.focus();m(b,this.id(a))||this.triggerChange()},updateSelection:function(a){var b=this.selection.find("span");this.selection.data("select2-data",a);b.empty();a=this.opts.formatSelection(a,b);a!==g&&b.append(this.opts.escapeMarkup(a));this.selection.removeClass("select2-default");this.opts.allowClear&&this.getPlaceholder()!==g&&this.selection.find("abbr").show()},val:function(){var a,b=null,c=this;if(0===arguments.length)return this.opts.element.val();
a=arguments[0];if(this.select)this.select.val(a).find(":selected").each2(function(a,c){b={id:c.attr("value"),text:c.text()};return!1}),this.updateSelection(b),this.setPlaceholder();else{if(this.opts.initSelection===g)throw Error("cannot call val() if initSelection() is not defined");a?(this.opts.element.val(a),this.opts.initSelection(this.opts.element,function(a){c.opts.element.val(!a?"":c.id(a));c.updateSelection(a);c.setPlaceholder()})):this.clear()}},clearSearch:function(){this.search.val("")},
data:function(a){var b;if(0===arguments.length)return b=this.selection.data("select2-data"),b==g&&(b=null),b;!a||""===a?this.clear():(this.opts.element.val(!a?"":this.id(a)),this.updateSelection(a))}});z=x(w,{createContainer:function(){return e("<div></div>",{"class":"select2-container select2-container-multi"}).html("    <ul class='select2-choices'>  <li class='select2-search-field'>    <input type='text' autocomplete='off' class='select2-input'>  </li></ul><div class='select2-drop select2-drop-multi' style='display:none;'>   <ul class='select2-results'>   </ul></div>")},
prepareOpts:function(){var a=this.parent.prepareOpts.apply(this,arguments);"select"===a.element.get(0).tagName.toLowerCase()&&(a.initSelection=function(a,c){var d=[];a.find(":selected").each2(function(a,b){d.push({id:b.attr("value"),text:b.text()})});e.isFunction(c)&&c(d)});return a},initContainer:function(){var a;this.searchContainer=this.container.find(".select2-search-field");this.selection=a=this.container.find(".select2-choices");this.search.bind("keydown",this.bind(function(b){if(this.enabled){if(b.which===
f.BACKSPACE&&""===this.search.val()){this.close();var c;c=a.find(".select2-search-choice-focus");if(0<c.length){this.unselect(c.first());this.search.width(10);l(b);return}c=a.find(".select2-search-choice");0<c.length&&c.last().addClass("select2-search-choice-focus")}else a.find(".select2-search-choice-focus").removeClass("select2-search-choice-focus");if(this.opened())switch(b.which){case f.UP:case f.DOWN:this.moveHighlight(b.which===f.UP?-1:1);l(b);return;case f.ENTER:case f.TAB:this.selectHighlighted();
l(b);return;case f.ESC:this.cancel(b);l(b);return}if(!(b.which===f.TAB||f.isControl(b)||f.isFunctionKey(b)||b.which===f.BACKSPACE||b.which===f.ESC)&&!(!1===this.opts.openOnEnter&&b.which===f.ENTER))this.open(),(b.which===f.PAGE_UP||b.which===f.PAGE_DOWN)&&l(b)}}));this.search.bind("keyup",this.bind(this.resizeSearch));this.search.bind("blur",this.bind(function(a){this.container.removeClass("select2-container-active");this.search.removeClass("select2-focused");this.clearSearch();a.stopImmediatePropagation()}));
this.container.delegate(".select2-choices","mousedown",this.bind(function(a){this.enabled&&!(0<e(a.target).closest(".select2-search-choice").length)&&(this.clearPlaceholder(),this.open(),this.focusSearch(),a.preventDefault())}));this.container.delegate(".select2-choices","focus",this.bind(function(){this.enabled&&(this.container.addClass("select2-container-active"),this.dropdown.addClass("select2-drop-active"),this.clearPlaceholder())}));this.clearSearch()},enable:function(){this.enabled||(this.parent.enable.apply(this,
arguments),this.search.removeAttr("disabled"))},disable:function(){this.enabled&&(this.parent.disable.apply(this,arguments),this.search.attr("disabled",!0))},initSelection:function(){""===this.opts.element.val()&&(this.updateSelection([]),this.close(),this.clearSearch());if(this.select||""!==this.opts.element.val()){var a=this;this.opts.initSelection.call(null,this.opts.element,function(b){if(b!==g&&b!==null){a.updateSelection(b);a.close();a.clearSearch()}})}},clearSearch:function(){var a=this.getPlaceholder();
a!==g&&0===this.getVal().length&&!1===this.search.hasClass("select2-focused")?(this.search.val(a).addClass("select2-default"),this.resizeSearch()):this.search.val(" ").width(10)},clearPlaceholder:function(){this.search.hasClass("select2-default")?this.search.val("").removeClass("select2-default"):" "===this.search.val()&&this.search.val("")},opening:function(){this.parent.opening.apply(this,arguments);this.clearPlaceholder();this.resizeSearch();this.focusSearch()},close:function(){this.opened()&&
this.parent.close.apply(this,arguments)},focus:function(){this.close();this.search.focus()},isFocused:function(){return this.search.hasClass("select2-focused")},updateSelection:function(a){var b=[],c=[],d=this;e(a).each(function(){0>i(d.id(this),b)&&(b.push(d.id(this)),c.push(this))});a=c;this.selection.find(".select2-search-choice").remove();e(a).each(function(){d.addSelectedChoice(this)});d.postprocessResults()},tokenize:function(){var a=this.search.val(),a=this.opts.tokenizer(a,this.data(),this.bind(this.onSelect),
this.opts);null!=a&&a!=g&&(this.search.val(a),0<a.length&&this.open())},onSelect:function(a){this.addSelectedChoice(a);this.select&&this.postprocessResults();this.opts.closeOnSelect?(this.close(),this.search.width(10)):0<this.countSelectableResults()?(this.search.width(10),this.resizeSearch(),this.positionDropdown()):this.close();this.triggerChange({added:a});this.focusSearch()},cancel:function(){this.close();this.focusSearch()},addSelectedChoice:function(a){var b=e("<li class='select2-search-choice'>    <div></div>    <a href='#' onclick='return false;' class='select2-search-choice-close' tabindex='-1'></a></li>"),
c=this.id(a),d=this.getVal(),f;f=this.opts.formatSelection(a,b);b.find("div").replaceWith("<div>"+this.opts.escapeMarkup(f)+"</div>");b.find(".select2-search-choice-close").bind("mousedown",l).bind("click dblclick",this.bind(function(a){this.enabled&&(e(a.target).closest(".select2-search-choice").fadeOut("fast",this.bind(function(){this.unselect(e(a.target));this.selection.find(".select2-search-choice-focus").removeClass("select2-search-choice-focus");this.close();this.focusSearch()})).dequeue(),
l(a))})).bind("focus",this.bind(function(){this.enabled&&(this.container.addClass("select2-container-active"),this.dropdown.addClass("select2-drop-active"))}));b.data("select2-data",a);b.insertBefore(this.searchContainer);d.push(c);this.setVal(d)},unselect:function(a){var b=this.getVal(),c,d,a=a.closest(".select2-search-choice");if(0===a.length)throw"Invalid argument: "+a+". Must be .select2-search-choice";c=a.data("select2-data");d=i(this.id(c),b);0<=d&&(b.splice(d,1),this.setVal(b),this.select&&
this.postprocessResults());a.remove();this.triggerChange({removed:c})},postprocessResults:function(){var a=this.getVal(),b=this.results.find(".select2-result-selectable"),c=this.results.find(".select2-result-with-children"),d=this;b.each2(function(b,c){var e=d.id(c.data("select2-data"));0<=i(e,a)?c.addClass("select2-disabled").removeClass("select2-result-selectable"):c.removeClass("select2-disabled").addClass("select2-result-selectable")});c.each2(function(a,b){0==b.find(".select2-result-selectable").length?
b.addClass("select2-disabled"):b.removeClass("select2-disabled")});b.each2(function(a,b){if(!b.hasClass("select2-disabled")&&b.hasClass("select2-result-selectable"))return d.highlight(0),!1})},resizeSearch:function(){var a,b,c,d,f=this.search.outerWidth()-this.search.width();a=this.search;q||(c=a[0].currentStyle||window.getComputedStyle(a[0],null),q=e("<div></div>").css({position:"absolute",left:"-10000px",top:"-10000px",display:"none",fontSize:c.fontSize,fontFamily:c.fontFamily,fontStyle:c.fontStyle,
fontWeight:c.fontWeight,letterSpacing:c.letterSpacing,textTransform:c.textTransform,whiteSpace:"nowrap"}),e("body").append(q));q.text(a.val());a=q.width()+10;b=this.search.offset().left;c=this.selection.width();d=this.selection.offset().left;b=c-(b-d)-f;b<a&&(b=c-f);40>b&&(b=c-f);this.search.width(b)},getVal:function(){var a;if(this.select)return a=this.select.val(),null===a?[]:a;a=this.opts.element.val();return s(a,this.opts.separator)},setVal:function(a){var b;this.select?this.select.val(a):(b=
[],e(a).each(function(){0>i(this,b)&&b.push(this)}),this.opts.element.val(0===b.length?"":b.join(this.opts.separator)))},val:function(){var a,b=[],c=this;if(0===arguments.length)return this.getVal();if(a=arguments[0])if(this.setVal(a),this.select)this.select.find(":selected").each(function(){b.push({id:e(this).attr("value"),text:e(this).text()})}),this.updateSelection(b);else{if(this.opts.initSelection===g)throw Error("val() cannot be called if initSelection() is not defined");this.opts.initSelection(this.opts.element,
function(a){var b=e(a).map(c.id);c.setVal(b);c.updateSelection(a);c.clearSearch()})}else this.opts.element.val(""),this.updateSelection([]);this.clearSearch()},onSortStart:function(){if(this.select)throw Error("Sorting of elements is not supported when attached to <select>. Attach to <input type='hidden'/> instead.");this.search.width(0);this.searchContainer.hide()},onSortEnd:function(){var a=[],b=this;this.searchContainer.show();this.searchContainer.appendTo(this.searchContainer.parent());this.resizeSearch();
this.selection.find(".select2-search-choice").each(function(){a.push(b.opts.id(e(this).data("select2-data")))});this.setVal(a);this.triggerChange()},data:function(a){var b=this,c;if(0===arguments.length)return this.selection.find(".select2-search-choice").map(function(){return e(this).data("select2-data")}).get();a||(a=[]);c=e.map(a,function(a){return b.opts.id(a)});this.setVal(c);this.updateSelection(a);this.clearSearch()}});e.fn.select2=function(){var a=Array.prototype.slice.call(arguments,0),b,
c,d,f,h="val destroy opened open close focus isFocused container onSortStart onSortEnd enable disable positionDropdown data".split(" ");this.each(function(){if(0===a.length||"object"===typeof a[0])b=0===a.length?{}:e.extend({},a[0]),b.element=e(this),"select"===b.element.get(0).tagName.toLowerCase()?f=b.element.attr("multiple"):(f=b.multiple||!1,"tags"in b&&(b.multiple=f=!0)),c=f?new z:new y,c.init(b);else if("string"===typeof a[0]){if(0>i(a[0],h))throw"Unknown method: "+a[0];d=g;c=e(this).data("select2");
if(c!==g&&(d="container"===a[0]?c.container:c[a[0]].apply(c,a.slice(1)),d!==g))return!1}else throw"Invalid arguments to select2 plugin: "+a;});return d===g?this:d};e.fn.select2.defaults={width:"copy",closeOnSelect:!0,openOnEnter:!0,containerCss:{},dropdownCss:{},containerCssClass:"",dropdownCssClass:"",formatResult:function(a,b,c){b=[];B(a.text,c.term,b);return b.join("")},formatSelection:function(a){return a?a.text:g},formatResultCssClass:function(){return g},formatNoMatches:function(){return"No matches found"},
formatInputTooShort:function(a,b){return"Please enter "+(b-a.length)+" more characters"},formatSelectionTooBig:function(a){return"You can only select "+a+" item"+(1==a?"":"s")},formatLoadMore:function(){return"Loading more results..."},formatSearching:function(){return"Searching..."},minimumResultsForSearch:0,minimumInputLength:0,maximumSelectionSize:0,id:function(a){return a.id},matcher:function(a,b){return 0<=b.toUpperCase().indexOf(a.toUpperCase())},separator:",",tokenSeparators:[],tokenizer:H,
escapeMarkup:function(a){return a&&"string"===typeof a?a.replace(/&/g,"&amp;"):a},blurOnChange:!1};window.Select2={query:{ajax:C,local:D,tags:E},util:{debounce:A,markMatch:B},"class":{"abstract":w,single:y,multi:z}}}})(jQuery);
function showMessage(B,A){$(B+"msg").innerHTML=A;
$(B).show()
}function error(A){showMessage("error",A)
}function warning(A){showMessage("warning",A)
}function info(A){showMessage("info",A)
}function toggleFav(B,A){$j.ajax({type:"POST",dataType:"json",url:baseUrl+"/favourites/toggle/"+B,success:function(D){var C=$j(A);
C.removeClass("fav notfav");
C.addClass(D.css);
C.attr("title",D.title)
}})
}function autocompleteResources(){$("searchInput").value="";
new Ajax.Autocompleter("searchInput","searchResourcesResults",baseUrl+"/search",{method:"post",minChars:3,indicator:"searchingResources",paramName:"s",updateElement:function(A){if(A.id){window.location=baseUrl+"/dashboard/index/"+A.id
}},onShow:function(A,B){B.show()
}})
}var SelectBox={cache:new Object(),init:function(E){var D=document.getElementById(E);
var C;
SelectBox.cache[E]=new Array();
var A=SelectBox.cache[E];
for(var B=0;
(C=D.options[B]);
B++){A.push({value:C.value,text:C.text,displayed:1})
}},redisplay:function(E){var D=document.getElementById(E);
D.options.length=0;
for(var B=0,A=SelectBox.cache[E].length;
B<A;
B++){var C=SelectBox.cache[E][B];
if(C.displayed){D.options[D.options.length]=new Option(C.text,C.value,false,false)
}}},filter:function(G,F){var E=F.toLowerCase().split(/\s+/);
var D,C;
for(var B=0;
(D=SelectBox.cache[G][B]);
B++){D.displayed=1;
for(var A=0;
(C=E[A]);
A++){if(D.text.toLowerCase().indexOf(C)==-1){D.displayed=0
}}}SelectBox.redisplay(G)
},delete_from_cache:function(F,E){var D,C=null;
for(var B=0;
(D=SelectBox.cache[F][B]);
B++){if(D.value==E){C=B;
break
}}var A=SelectBox.cache[F].length-1;
for(var B=C;
B<A;
B++){SelectBox.cache[F][B]=SelectBox.cache[F][B+1]
}SelectBox.cache[F].length--
},add_to_cache:function(B,A){SelectBox.cache[B].push({value:A.value,text:A.text,displayed:1})
},cache_contains:function(D,C){var B;
for(var A=0;
(B=SelectBox.cache[D][A]);
A++){if(B.value==C){return true
}}return false
},move:function(E,D){var A=document.getElementById(E);
var C;
for(var B=0;
(C=A.options[B]);
B++){if(C.selected&&SelectBox.cache_contains(E,C.value)){SelectBox.add_to_cache(D,{value:C.value,text:C.text,displayed:1});
SelectBox.delete_from_cache(E,C.value)
}}SelectBox.redisplay(E);
SelectBox.redisplay(D)
},move_all:function(E,D){var A=document.getElementById(E);
var C;
for(var B=0;
(C=A.options[B]);
B++){if(SelectBox.cache_contains(E,C.value)){SelectBox.add_to_cache(D,{value:C.value,text:C.text,displayed:1});
SelectBox.delete_from_cache(E,C.value)
}}SelectBox.redisplay(E);
SelectBox.redisplay(D)
},sort:function(A){SelectBox.cache[A].sort(function(C,B){C=C.text.toLowerCase();
B=B.text.toLowerCase();
try{if(C>B){return 1
}if(C<B){return -1
}}catch(D){}return 0
})
},select_all:function(C){var B=document.getElementById(C);
for(var A=0;
A<B.options.length;
A++){B.options[A].selected="selected"
}}};
var treemaps={};
function treemapById(A){return treemaps[A]
}var TreemapContext=function(B,A){this.rid=B;
this.label=A
};
var Treemap=function(D,C,A,B){this.id=D;
this.sizeMetric=C;
this.colorMetric=A;
this.breadcrumb=[];
treemaps[D]=this;
this.rootNode().height(this.rootNode().width()*B/100);
this.initNodes()
};
Treemap.prototype.currentContext=function(){if(this.breadcrumb.length>0){return this.breadcrumb[this.breadcrumb.length-1]
}return null
};
Treemap.prototype.load=function(){var C=this.currentContext();
var B="";
this.breadcrumb.each(function(D){B+=D.label+"&nbsp;/&nbsp;"
});
$j("#tm-bc-"+this.id).html(B);
$j("#tm-loading-"+this.id).show();
var A=this;
$j.ajax({type:"GET",url:baseUrl+"/treemap/index?html_id="+this.id+"&size_metric="+this.sizeMetric+"&color_metric="+this.colorMetric+"&resource="+C.rid,dataType:"html",success:function(D){A.rootNode().html(D);
A.initNodes();
$j("#tm-loading-"+A.id).hide()
}})
};
Treemap.prototype.rootNode=function(){return $j("#tm-"+this.id)
};
Treemap.prototype.initNodes=function(){var A=this;
$j("#tm-"+this.id).find("a").each(function(B){$j(this).on("click",function(C){C.stopPropagation()
})
});
$j("#tm-"+this.id).find("[rid]").each(function(B){$j(this).on("contextmenu",function(C){C.stopPropagation();
C.preventDefault();
if(A.breadcrumb.length>1){A.breadcrumb.pop();
A.load()
}else{if(A.breadcrumb.length==1){$j("#tm-loading-"+A.id).show();
location.reload()
}}return false
});
$j(this).on("click",function(F){var G=$j(this);
var E=G.attr("rid");
var C=!!(G.attr("l"));
if(!C){var D=new TreemapContext(E,G.text());
A.breadcrumb.push(D);
A.load()
}})
})
};
(function(A){A.fn.extend({modal:function(){return this.each(function(){var C=A(this);
var B=C.bind("click",function(){if(A("#modal").length){return 
}var E=A('<div id="modal" class="ui-widget-overlay"></div>').appendTo("body");
var D=B.attr("modal-url")||B.attr("href");
A.get(D,function(F){E.removeClass("ui-widget-overlay");
E.html(F);
E.dialog({width:(B.attr("modal-width")||540),draggable:false,autoOpen:false,modal:true,minHeight:50,resizable:false,close:function(){A("#modal").remove()
}});
E.dialog("open")
}).error(function(){alert("Server error. Please contact your administrator.")
}).complete(function(){E.removeClass("ui-widget-overlay")
});
B.click(function(){E.dialog("open");
return false
});
return false
})
})
},modalForm:function(B){return this.each(function(){var C=A(this);
C.submit(function(D){A("input[type=submit]",this).attr("disabled","disabled");
A.ajax(A.extend({type:"POST",url:C.attr("action"),data:C.serialize(),success:function(E){window.location.reload()
},error:function(F,G,E){A("#modal").html(F.responseText)
}},B));
return false
})
})
}})
})(jQuery);
function closeModalWindow(){$j("#modal").dialog("close");
return false
}function supports_html5_storage(){try{return"localStorage" in window&&window.localStorage!==null
}catch(A){return false
}}var currentlyDisplayedDropdownMenu;
var hideCurrentDropdownMenu=function(){menu=$j("#"+currentlyDisplayedDropdownMenu);
if(menu){menu.hide()
}$j(document).unbind("mouseup",hideCurrentDropdownMenu)
};
var clickOnDropdownMenuLink=function(C){var B=$j(C.target).children("a");
if(B){var A=B.attr("href");
if(A&&A.length>1){window.location=A
}else{B.click()
}}};
function showDropdownMenu(A){if(A==currentlyDisplayedDropdownMenu){currentlyDisplayedDropdownMenu=""
}else{currentlyDisplayedDropdownMenu=A;
$j(document).mouseup(hideCurrentDropdownMenu);
$j("#"+currentlyDisplayedDropdownMenu+" li").unbind("click");
$j("#"+currentlyDisplayedDropdownMenu+" li").click(clickOnDropdownMenuLink);
$j("#"+currentlyDisplayedDropdownMenu).show()
}};var Portal=Class.create();
Portal.prototype={initialize:function(A){this.setOptions(A);
if(!this.options.editorEnabled){return 
}Droppables.add(this.options.blocklist,{containment:$A($$("."+this.options.column)),hoverclass:this.options.hoverclass,overlap:"horizontal",onDrop:function(B,C){$(B).remove()
}});
this.createAllSortables();
this.lastSaveString="";
this.saveDashboardsState()
},createAllSortables:function(){var A=$$("."+this.options.column);
$A(A).each(function(B){Sortable.create(B,{containment:$A(A),constraint:false,tag:"div",handle:this.options.handleClass,only:this.options.block,dropOnEmpty:true,hoverclass:this.options.hoverclass,starteffect:function(C){$(C).addClassName("shadow-block")
}.bind(this),endeffect:function(C){$(C).removeClassName("shadow-block")
}.bind(this),onUpdate:function(){this.saveDashboardsState()
}.bind(this)})
}.bind(this))
},highlightWidget:function(A){new Effect.Highlight($("block_"+A),{duration:this.options.highlight_duration,startcolor:this.options.highlight_startcolor,endcolor:this.options.highlight_endcolor})
},saveDashboardsState:function(){var A="";
var C=1;
$$("."+this.options.column).each(function(G){if($(G).select("."+this.options.block).length==0){$(G).select("."+this.options.columnhandle)[0].show()
}else{$(G).select("."+this.options.columnhandle)[0].hide()
}if(C>1){A+=";"
}A+=Sortable.sequence($(G).identify());
C++
});
if(A==this.lastSaveString){return 
}var F=this.lastSaveString=="";
this.lastSaveString=A;
if(F){return 
}try{if($(this.options.dashboardstate)){$(this.options.dashboardstate).value=A
}if(this.options.saveurl){var B=this.options.saveurl;
var D=this.options.dashboardstate+"="+escape(A);
new Ajax.Request(B,{evalscripts:false,method:"post",postBody:D})
}}catch(E){}},setOptions:function(A){this.options={};
Object.extend(this.options,A||{})
},editWidget:function(A){$("widget_title_"+A)&&$("widget_title_"+A).hide();
$("widget_"+A).hide();
$("widget_props_"+A).show()
},cancelEditWidget:function(A){$("widget_title_"+A)&&$("widget_title_"+A).show();
$("widget_"+A).show();
$("widget_props_"+A).hide()
},deleteWidget:function(A){$(A).up("."+this.options.block).remove();
this.saveDashboardsState()
}};
autoResize=function(C,B){var A=null;
Event.observe(window,"resize",function(){if(A==null){A=window.setTimeout(function(){A=null;
B()
},C)
}})
};function updateDuplicationLines(C,D,F,E,B,A){$$("#duplGroup_"+D+" p.selected").invoke("removeClassName","selected");
$("duplCount-"+D+"-"+F).addClassName("selected");
$("duplFrom-"+D+"-"+F).addClassName("selected");
$("duplName-"+D+"-"+F).addClassName("selected");
$("duplLoading-"+D).addClassName("loading");
if($("source-"+D).childElements()[0].hasClassName("expanded")){A=B+E-1
}new Ajax.Updater("source-"+D,C+"&to_line="+A+"&from_line="+B+"&lines_count="+E+"&group_index="+D,{asynchronous:true,evalScripts:true});
return false
};function loadResourceViewer(C,B,A){$("resource_loading").show();
if(A==undefined){A=true
}new Ajax.Updater("resource_container",baseUrl+"/resource/index/"+C+"?tab="+B+"&display_title="+A,{asynchronous:true,evalScripts:true});
return false
}function loadGWT(E,G,D,C,F,B,A){config.resource=[{id:G,key:D,name:C,scope:F,qualifier:B,lang:A}];
config.resource_key=G;
modules[E]()
}function cancelViolationAction(A){new Ajax.Updater("vId"+A,baseUrl+"/reviews/display_violation/"+A,{asynchronous:true,evalScripts:true});
return false
}function hideMoreViolationActions(B){var A=$("more"+B);
if(A!=null){A.hide()
}}function sCF(A){hideMoreViolationActions(A);
new Ajax.Updater("reviewForm"+A,baseUrl+"/reviews/violation_comment_form/"+A,{asynchronous:true,evalScripts:true,onComplete:function(B){$("vActions"+A).remove();
$("reviewForm"+A).show();
$("commentText"+A).focus()
}});
return false
}function sS(A,B){hideMoreViolationActions(A);
new Ajax.Updater("reviewForm"+A,baseUrl+"/reviews/screen/"+A+"?command="+B,{asynchronous:true,evalScripts:true,onComplete:function(C){$("vActions"+A).remove();
$("reviewForm"+A).show();
$("commentText"+A).focus()
}});
return false
}function sCSF(A){hideMoreViolationActions(A);
new Ajax.Updater("reviewForm"+A,baseUrl+"/reviews/violation_change_severity_form/"+A,{asynchronous:true,evalScripts:true,onComplete:function(B){$("vActions"+A).remove();
$("reviewForm"+A).show();
$("selectSeverity"+A).focus()
}});
return false
}function sCStF(A){hideMoreViolationActions(A);
new Ajax.Updater("reviewForm"+A,baseUrl+"/reviews/violation_change_status_form/"+A,{asynchronous:true,evalScripts:true,onComplete:function(B){$("vActions"+A).remove();
$("reviewForm"+A).show();
$("commentText"+A).focus()
}});
return false
}function sFPF(A){hideMoreViolationActions(A);
new Ajax.Updater("reviewForm"+A,baseUrl+"/reviews/violation_false_positive_form/"+A,{asynchronous:true,evalScripts:true,onComplete:function(B){$("vActions"+A).remove();
$("reviewForm"+A).show();
$("commentText"+A).focus()
}});
return false
}function sAF(A){hideMoreViolationActions(A);
new Ajax.Updater("reviewForm"+A,baseUrl+"/reviews/violation_assign_form/"+A,{asynchronous:true,evalScripts:true,onComplete:function(B){$("vActions"+A).remove();
$("reviewForm"+A).show();
$("assignee_login").focus()
}});
return false
}function sAPF(A){hideMoreViolationActions(A);
new Ajax.Updater("reviewForm"+A,baseUrl+"/reviews/violation_action_plan_form/"+A,{asynchronous:true,evalScripts:true,onComplete:function(B){$("vActions"+A).remove();
$("reviewForm"+A).show();
$("action_plan").focus()
}});
return false
}function sVF(D,A,B,C){row=$("createViolationForm"+A);
if(row==null){new Ajax.Updater("pos"+A,baseUrl+"/resource/show_create_violation_form",{parameters:{resource:D,line:A,gray_colspan:B,white_colspan:C},asynchronous:true,evalScripts:true,insertion:"after"})
}return false
}function hVF(A){row=$("createViolationRow"+A);
if(row!=null){row.remove()
}return false
}function expandTests(A){$j(".tests_viewer #test_collapseLink_"+A).show();
$j(".tests_viewer #test_expandLink_"+A).hide();
$j(".tests_viewer #test_message_"+A).show()
}function collapseTests(A){$j(".tests_viewer #test_collapseLink_"+A).hide();
$j(".tests_viewer #test_expandLink_"+A).show();
$j(".tests_viewer #test_message_"+A).hide()
};window.Sonar = {};

Sonar.RecentHistory = function () {
};

Sonar.RecentHistory.prototype.getRecentHistory = function() {
  var sonarHistory = localStorage.getItem("sonar_recent_history");
  if (sonarHistory == null) {
    sonarHistory = new Array();
  } else {
    sonarHistory = JSON.parse(sonarHistory);
  }
  return sonarHistory;
};
  
Sonar.RecentHistory.prototype.clear = function () {
  localStorage.clear();
};
  
Sonar.RecentHistory.prototype.add = function (resourceKey, resourceName, iconPath) {
  var sonarHistory = this.getRecentHistory();
  
  if (resourceKey != '') {
    var newEntry = {'key': resourceKey, 'name': resourceName, 'iconPath': iconPath};
    // removes the element of the array if it exists
    for (i = 0; i < sonarHistory.length; i++) {
      var item = sonarHistory[i];
      if (item['key'] == resourceKey) {
        sonarHistory.splice(i, 1);
        break;
      }
    }    
    // then add it to the beginning of the array
    sonarHistory.unshift(newEntry);
    // and finally slice the array to keep only 10 elements
    sonarHistory = sonarHistory.slice(0,10);
    
    localStorage.setItem("sonar_recent_history", JSON.stringify(sonarHistory));
  }
};

Sonar.RecentHistory.prototype.populateRecentHistoryPanel = function () {
  var historyLinksList = $j('#recent-history-list');
  historyLinksList.empty();
  
  var recentHistory = this.getRecentHistory();  
  if (recentHistory.length == 0) {
    $j("#recent-history").hide();
  } else {    
    recentHistory.forEach(function (resource) {
      historyLinksList.append('<li><img width="16" height="16" src="'
                            + baseUrl
                            + resource['iconPath']
                            + '"><a href="'
                            + baseUrl
                            + '/dashboard/index/'
                            + resource['key']
                            + '"> ' 
                            + resource['name'] 
                            + '</a></li>');
    });
    $j("#recent-history").show();
  }
};
