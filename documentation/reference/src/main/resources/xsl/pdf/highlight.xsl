<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xslthl="http://xslthl.sf.net" exclude-result-prefixes="xslthl" version="1.0">

	<xsl:template match='xslthl:keyword' mode="xslthl">
		<fo:inline font-weight="bold" color="#7F0055">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:string' mode="xslthl">
		<fo:inline color="#2A00FF">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:comment' mode="xslthl">
		<fo:inline color="#3F7F5F">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:value' mode="xslthl">
		<fo:inline color="black">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:tag' mode="xslthl">
		<fo:inline font-weight="bold" color="#000099">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:attribute' mode="xslthl">
		<fo:inline color="#009900">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:char' mode="xslthl">
		<fo:inline font-style="italic" color="yellow">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:number' mode="xslthl">
		<fo:inline color="#006666">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:annotation' mode="xslthl">
		<fo:inline color="gray">
			<xsl:apply-templates mode="xslthl" />
		</fo:inline>
	</xsl:template>

	<xsl:template match='xslthl:directive' mode="xslthl">
		<xsl:apply-templates mode="xslthl" />
	</xsl:template>

</xsl:stylesheet>
