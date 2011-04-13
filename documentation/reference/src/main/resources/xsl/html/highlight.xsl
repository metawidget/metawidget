<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xslthl="http://xslthl.sf.net" exclude-result-prefixes="xslthl" version="1.0">

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

	<xsl:template match='xslthl:value' mode="xslthl">
		<span class="atv">
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

	<xsl:template match='xslthl:char' mode="xslthl">
		<span class="lit">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:number' mode="xslthl">
		<span class="dec">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:annotation' mode="xslthl">
		<span class="pun">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

	<xsl:template match='xslthl:directive' mode="xslthl">
		<span class="com">
			<xsl:apply-templates mode="xslthl" />
		</span>
	</xsl:template>

</xsl:stylesheet>
