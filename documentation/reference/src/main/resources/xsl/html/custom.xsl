<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:d="http://docbook.org/ns/docbook" version="1.0">

	<xsl:import href="urn:docbkx:stylesheet" />

	<!-- Code blocks -->

	<xsl:import href="urn:docbkx:stylesheet/highlight.xsl" />
	<xsl:import href="highlight.xsl" />
	<xsl:output indent="no" />
	<xsl:param name="highlight.source" select="1" />

	<xsl:attribute-set name="verbatim.properties">
		<xsl:attribute name="space-before.minimum">1em</xsl:attribute>
		<xsl:attribute name="space-before.optimum">1em</xsl:attribute>
		<xsl:attribute name="space-before.maximum">1em</xsl:attribute>

		<xsl:attribute name="border-color">#444444</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
		<xsl:attribute name="border-width">0.1pt</xsl:attribute>
		<xsl:attribute name="padding-top">0.5em</xsl:attribute>
		<xsl:attribute name="padding-left">0.5em</xsl:attribute>
		<xsl:attribute name="padding-right">0.5em</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.5em</xsl:attribute>
		<xsl:attribute name="margin-left">0.1em</xsl:attribute>
		<xsl:attribute name="margin-right">0.1em</xsl:attribute>

		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:param name="shade.verbatim" select="1" />

	<xsl:attribute-set name="shade.verbatim.style">
		<xsl:attribute name="background-color">#f0f0f0</xsl:attribute>
	</xsl:attribute-set>

	<!-- CSS -->
	
	<xsl:param name="html.stylesheet">../shared/css/stylesheet.css</xsl:param>
	<xsl:param name="html.stylesheet.type">text/css</xsl:param>
	<xsl:param name="table.borders.with.css" select="1" />
	
	<!-- Page -->

	<xsl:param name="page.margin.top" select="'1cm'" />
	<xsl:param name="region.before.extent" select="'1cm'" />
	<xsl:param name="body.margin.top" select="'1cm'" />
	<xsl:param name="body.start.indent">
		0pt
	</xsl:param>

	<xsl:param name="body.margin.bottom" select="'1cm'" />
	<xsl:param name="region.after.extent" select="'1cm'" />
	<xsl:param name="page.margin.bottom" select="'1cm'" />
	<xsl:param name="title.margin.left" select="'0cm'" />

	<xsl:param name="page.margin.inner" select="'2cm'" />
	<xsl:param name="page.margin.outer" select="'2cm'" />

	<!-- Fonts -->

	<xsl:param name="hyphenate" select="false" />
	<xsl:param name="line-height" select="1.4" />

	<xsl:attribute-set name="root.properties">
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="normal.para.spacing">
		<xsl:attribute name="space-before.optimum">2mm</xsl:attribute>
		<xsl:attribute name="space-before.minimum">2mm</xsl:attribute>
		<xsl:attribute name="space-before.maximum">2mm</xsl:attribute>
		<xsl:attribute name="space-after.optimum">2mm</xsl:attribute>
		<xsl:attribute name="space-after.minimum">2mm</xsl:attribute>
		<xsl:attribute name="space-after.maximum">2mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="component.title.properties">
		<xsl:attribute name="font-size">24pt</xsl:attribute>
		<xsl:attribute name="keep-with-next">always</xsl:attribute>
		<xsl:attribute name="border-bottom">1mm solid black</xsl:attribute>
		<xsl:attribute name="padding-top">15mm</xsl:attribute>
		<xsl:attribute name="space-after.optimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.minimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.maximum">3mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="section.title.level1.properties">
		<xsl:attribute name="font-size">14pt</xsl:attribute>
		<xsl:attribute name="border-bottom">0.5mm solid black</xsl:attribute>
		<xsl:attribute name="keep-with-next">always</xsl:attribute>
		<xsl:attribute name="space-before">10mm</xsl:attribute>
		<xsl:attribute name="space-after.optimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.minimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.maximum">3mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="section.title.level2.properties">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="border-bottom">0.25mm solid black</xsl:attribute>
		<xsl:attribute name="keep-with-next">always</xsl:attribute>
		<xsl:attribute name="space-before">5mm</xsl:attribute>
		<xsl:attribute name="space-after.optimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.minimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.maximum">3mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="section.title.level3.properties">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="border-bottom">0.25mm solid black</xsl:attribute>
		<xsl:attribute name="keep-with-next">always</xsl:attribute>
		<xsl:attribute name="space-before">5mm</xsl:attribute>
		<xsl:attribute name="space-after.optimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.minimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.maximum">3mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="section.title.level4.properties">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="border-bottom">0.25mm dashed black</xsl:attribute>
		<xsl:attribute name="keep-with-next">always</xsl:attribute>
		<xsl:attribute name="space-before">5mm</xsl:attribute>
		<xsl:attribute name="space-after.optimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.minimum">3mm</xsl:attribute>
		<xsl:attribute name="space-after.maximum">3mm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:template match="d:filename|d:classname|d:function|d:guibutton">
		<tt>
			<xsl:call-template name="inline.charseq" />
		</tt>
	</xsl:template>

	<xsl:template match="d:parameter">
		<tt>
			<xsl:apply-templates />
		</tt>
	</xsl:template>

	<xsl:template match="d:symbol">
		<span style="color: red">
			<xsl:apply-templates />
		</span>
	</xsl:template>

	<xsl:template match="d:emphasis[@role='strike']">
		<span style="text-decoration: line-through">
			<xsl:apply-templates />
		</span>
	</xsl:template>

	<!-- Graphics -->
	
	<xsl:param name="ignore.image.scaling" select="1"></xsl:param>
	<xsl:param name="img.src.path">../shared/</xsl:param>
	
	<!-- Admonitions -->

	<xsl:param name="admon.graphics" select="1" />
	<xsl:param name="admon.graphics.path">../shared/images/admons/html/</xsl:param>
	<xsl:param name="admon.graphics.extension">.png</xsl:param>

	<!-- Callouts -->

	<xsl:param name="callout.graphics" select="1" />
	<xsl:param name="callout.graphics.path">../shared/images/callouts/</xsl:param>
	<xsl:param name="callout.graphics.extension">.gif</xsl:param>

	<!-- Table of Contents -->

	<xsl:param name="fop1.extensions" select="1" />
	<xsl:param name="autotoc.label.separator" select="'.  '" />
	<xsl:param name="toc.section.depth" select="5" />
	<xsl:param name="chapter.autolabel" select="1" />
	<xsl:param name="section.autolabel" select="1" />
	<xsl:param name="section.autolabel.max.depth" select="2" />
	<xsl:param name="section.label.includes.component.label" select="1" />
	<xsl:param name="table.footnote.number.format" select="'1'" />

	<xsl:param name="generate.toc">
		book toc,title
	</xsl:param>

	<!-- Remove "Chapter" from the Chapter titles -->

	<xsl:param name="local.l10n.xml" select="document('')" />
	<l:i18n xmlns:l="http://docbook.sourceforge.net/xmlns/l10n/1.0">
		<l:l10n language="en">
			<l:context name="title-numbered">
				<l:template name="chapter" text="%n.&#160;%t" />
				<l:template name="section" text="%n&#160;%t" />
			</l:context>
			<l:context name="title">
				<l:template name="example" text="Example&#160;%n&#160;%t" />
			</l:context>
			<!-- Remove title from Figure XRefs -->
			<l:context name="xref-number-and-title">
				<l:template name="figure" text="Figure %n" />
			</l:context>
		</l:l10n>
	</l:i18n>

	<!-- Cover -->

	<xsl:template name="book.titlepage.recto">
		<div class="coverpage">
			<h1><xsl:value-of select="d:title"/></h1>
			<h2><xsl:value-of select="d:bookinfo/d:releaseinfo"/></h2>
			<h3>
				<xsl:value-of select="d:bookinfo/d:author/d:firstname"/>
				<xsl:text> </xsl:text>
				<xsl:value-of select="d:bookinfo/d:author/d:surname"/>
			</h3>
		</div>
		<hr/>
	</xsl:template>
	
	<xsl:template name="book.titlepage.before.verso"/>
	<xsl:template name="book.titlepage.verso"/>
	<xsl:template name="book.titlepage.separator"/>	

	<!-- Figures -->

	<xsl:param name="formal.title.placement">
		figure after example after equation after table after procedure after
	</xsl:param>

	<xsl:attribute-set name="formal.title.properties" use-attribute-sets="normal.para.spacing">
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-style">italic</xsl:attribute>
		<xsl:attribute name="hyphenate">false</xsl:attribute>
		<xsl:attribute name="space-before.minimum">0.1em</xsl:attribute>
		<xsl:attribute name="space-before.optimum">0.1em</xsl:attribute>
		<xsl:attribute name="space-before.maximum">0.1em</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="informalfigure.properties">
		<xsl:attribute name="text-align">center</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="figure.properties">
		<xsl:attribute name="text-align">center</xsl:attribute>
	</xsl:attribute-set>
	
	<!-- Override title heading to print <em> instead of <b> -->
	<xsl:template name="formal.object.heading">
		<xsl:param name="object" select="." />
		<xsl:param name="title">
			<xsl:apply-templates select="$object" mode="object.title.markup">
				<xsl:with-param name="allow-anchors" select="1" />
			</xsl:apply-templates>
		</xsl:param>

		<p class="title">
			<em>
				<xsl:copy-of select="$title" />
			</em>
		</p>
	</xsl:template>	

	<!-- Tables -->

	<xsl:attribute-set name="table.cell.padding">
		<xsl:attribute name="padding-left">4pt</xsl:attribute>
		<xsl:attribute name="padding-right">4pt</xsl:attribute>
		<xsl:attribute name="padding-top">4pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">4pt</xsl:attribute>
	</xsl:attribute-set>

	<!-- Only hairlines as frame and cell borders in tables -->
	<xsl:param name="table.frame.border.thickness">
		0.1pt
	</xsl:param>
	<xsl:param name="table.cell.border.thickness">
		0.1pt
	</xsl:param>

	<xsl:template name="table.cell.properties">
		<xsl:choose>
			<xsl:when test="ancestor::d:thead">
				<xsl:attribute name="font-weight">bold</xsl:attribute>
				<xsl:attribute name="color">white</xsl:attribute>
				<xsl:attribute name="background-color">black</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="ancestor::d:tr[count(preceding-sibling::d:tr) mod 2 = 0]">
					<xsl:attribute name="background-color">#f0f0f0</xsl:attribute>
				</xsl:if>
				<xsl:if test="local-name() = 'th'">
					<xsl:attribute name="font-weight">bold</xsl:attribute>
					<xsl:attribute name="background-color">#888888</xsl:attribute>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Hide the URLs of external links (<ulink>) -->

	<xsl:param name="ulink.show" select="0" />
	<xsl:param name="ulink.target">_blank</xsl:param>	

	<!-- Add page numbers to bookmarks (<link>) -->

	<xsl:param name="insert.link.page.number" select="yes" />

</xsl:stylesheet>
