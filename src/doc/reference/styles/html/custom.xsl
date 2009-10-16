<?xml version="1.0" encoding="UTF-8"?>

	<!--
		Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
		writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
	-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xslthl="http://xslthl.sf.net" exclude-result-prefixes="xslthl" version='1.0'>

	<xsl:import href="file:///@dbf.xsl@/highlighting/common.xsl" />
	<xsl:import href="file:///@dbf.xsl@/html/highlight.xsl" />

	<xsl:param name="ulink.target">_blank</xsl:param>

	<!-- Activate Graphics -->
	<xsl:param name="admon.graphics" select="1" />
	<xsl:param name="admon.graphics.path">../shared/images/</xsl:param>
	<xsl:param name="admon.graphics.extension">.gif</xsl:param>
	<xsl:param name="ignore.image.scaling" select="1"></xsl:param>

	<xsl:param name="table.borders.with.css" select="1" />
	<xsl:param name="html.stylesheet">../shared/css/stylesheet.css</xsl:param>
	<xsl:param name="html.stylesheet.type">text/css</xsl:param>
	<xsl:param name="generate.toc">book toc</xsl:param>
	<xsl:param name="toc.section.depth">5</xsl:param>

	<xsl:param name="admonition.title.properties">text-align: left</xsl:param>

	<!--###################################################
                          Misc
    ################################################### -->

	<!-- Label Chapters and Sections (numbering) -->
	<xsl:param name="chapter.autolabel" select="1" />
	<xsl:param name="section.autolabel" select="1" />
	<xsl:param name="section.autolabel.max.depth" select="5" />

	<xsl:param name="section.label.includes.component.label" select="1" />
	<xsl:param name="table.footnote.number.format" select="'1'" />

	<!-- Remove "Chapter" from the Chapter titles... -->
	<xsl:param name="local.l10n.xml" select="document('')" />
	<l:i18n xmlns:l="http://docbook.sourceforge.net/xmlns/l10n/1.0">
		<l:l10n language="en">
			<l:context name="title-numbered">
				<l:template name="chapter" text="%n&#160;%t" />
				<l:template name="section" text="%n&#160;%t" />
			</l:context>
			<!-- Remove title from Figure XRefs -->

			<l:context name="xref-number-and-title">
				<l:template name="figure" text="Figure %n" />
			</l:context>
		</l:l10n>
	</l:i18n>

	<!-- Override title heading to print <i> instead of <b> -->
	<xsl:template name="formal.object.heading">
		<xsl:param name="object" select="." />
		<xsl:param name="title">
			<xsl:apply-templates select="$object" mode="object.title.markup">
				<xsl:with-param name="allow-anchors" select="1" />
			</xsl:apply-templates>
		</xsl:param>

		<p class="title">
			<i>
				<xsl:copy-of select="$title" />
			</i>
		</p>
	</xsl:template>

	<!-- Placement of titles -->
	<xsl:param name="formal.title.placement">
		figure after
		example after
		equation before
		table before
		procedure before
	</xsl:param>

	<!-- Remove callout column width. Default is 5% -->
	<xsl:template match="callout">
		<tr>
			<xsl:call-template name="tr.attributes">
				<xsl:with-param name="rownum">
					<xsl:number from="calloutlist" count="callout" />
				</xsl:with-param>
			</xsl:call-template>

			<td valign="top" align="left">
				<xsl:call-template name="anchor" />
				<xsl:call-template name="callout.arearefs">
					<xsl:with-param name="arearefs" select="@arearefs" />
				</xsl:call-template>
			</td>
			<td valign="top" align="left">
				<xsl:apply-templates />
			</td>
		</tr>
	</xsl:template>

	<!-- For external links (<ulink>), set the class attribute to "external" -->
	<xsl:template match="ulink" name="ulink">
		<xsl:variable name="link">
			<a>
				<xsl:if test="@id">
					<xsl:attribute name="name">
            <xsl:value-of select="@id" />
          </xsl:attribute>
				</xsl:if>
				<xsl:attribute name="class">external</xsl:attribute>
				<!--<xsl:attribute name="title">
          <xsl:value-of select="@url"/>
        </xsl:attribute>-->
				<xsl:attribute name="href">
          <xsl:value-of select="@url" />
        </xsl:attribute>
				<xsl:if test="$ulink.target != ''">
					<xsl:attribute name="target">
            <xsl:value-of select="$ulink.target" />
          </xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="count(child::node())=0">
						<xsl:value-of select="@url" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates />
					</xsl:otherwise>
				</xsl:choose>
			</a>
		</xsl:variable>

		<xsl:copy-of select="$link" />
	</xsl:template>

	<!--
  This section enables source highlighting and custom colors
  -->
	<xsl:param name="highlight.source" select="1" />
	<xsl:output indent="no" />
	<xsl:param name="highlight.default.language">
		java
	</xsl:param>

	<!--
  Ant will automatically replace @dbf.xsl@ with the path to
  the config at runtime
  -->
	<xsl:param name="highlight.xslthl.config">
		file:///@dbf.xsl@/highlighting/xslthl-config.xml
	</xsl:param>

	<xsl:template match='xslthl:keyword' mode="xslthl">
		<span class="kwd">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:string' mode="xslthl">
		<span class="str">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:comment' mode="xslthl">
		<span class="com">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:directive' mode="xslthl">
		<span class="dec">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:tag' mode="xslthl">
		<span class="tag">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:attribute' mode="xslthl">
		<span class="atn">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:value' mode="xslthl">
		<span class="pln">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:number' mode="xslthl">
		<span class="lit">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:annotation' mode="xslthl">
		<span class="pun">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<!-- Not sure which element will be in final XSLTHL 2.0 -->
	<xsl:template match='xslthl:doccomment|xslthl:doctype' mode="xslthl">
		<span class="com">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:html' mode="xslthl">
		<b>
			<i style="color: red">
				<xsl:apply-templates mode="xslthl" />
			</i>
		</b>
	</xsl:template>

	<xsl:template match='xslthl:xslt' mode="xslthl">
		<b style="color: #0066FF">
			<xsl:apply-templates mode="xslthl" />
		</b>
	</xsl:template>

</xsl:stylesheet>
