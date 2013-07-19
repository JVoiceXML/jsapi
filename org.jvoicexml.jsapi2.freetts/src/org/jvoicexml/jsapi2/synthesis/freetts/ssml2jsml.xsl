<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" version="1.0" indent="yes" encoding="iso-8859-1" media-type="application/rss+xml"/>

<xsl:template match="/">
  <jsml>
  	<xsl:attribute name="lang">
  		<xsl:value-of select="@xml:lang" />
  	</xsl:attribute>

  	<xsl:apply-templates />
  </jsml>
</xsl:template>

<xsl:template match="p">
      <div type="paragraph">
      	<xsl:apply-templates />
      </div>
</xsl:template>

<xsl:template match="s">
	<div type="sentence">
		<xsl:apply-templates />
	</div>
</xsl:template>

<xsl:template match="voice">
<voice>
	<xsl:attribute name="gender">
		<xsl:value-of select="@gender"/>
	</xsl:attribute>
	<xsl:attribute name="age">
		<xsl:value-of select="@age"/>
	</xsl:attribute>
	<xsl:attribute name="variant">
		<xsl:value-of select="@variant"/>
	</xsl:attribute>
	<xsl:attribute name="name">
		<xsl:value-of select="@name"/>
	</xsl:attribute>
	<xsl:apply-templates />
</voice>
</xsl:template>

<xsl:template match="prosody">
<prosody>
	<xsl:attribute name="rate">
		<xsl:value-of select="@rate"/>
	</xsl:attribute>
	<xsl:attribute name="volume">
		<xsl:value-of select="@volume"/>
	</xsl:attribute>
	<xsl:attribute name="pitch">
		<xsl:value-of select="@pitch"/>
	</xsl:attribute>
	<xsl:attribute name="range">
		<xsl:value-of select="@range"/>
	</xsl:attribute>
	<xsl:apply-templates />
</prosody>
</xsl:template>

<xsl:template match="emphasis">
<emphasis>
	<xsl:if test="@level!=''">
		<xsl:attribute name="level">
			<xsl:value-of select="@level" />
		</xsl:attribute>
	</xsl:if>
	<xsl:apply-templates />
</emphasis>
</xsl:template>

<xsl:template match="break">
<break>
	<xsl:if test="@time!=''">
		<xsl:attribute name="time">
			<xsl:value-of select="@time" />
		</xsl:attribute>
	</xsl:if>
	<xsl:if test="@strength!=''">
		<xsl:attribute name="size">
			<xsl:choose>
		        <xsl:when test="@strength='weak'">
		        	<xsl:text>small</xsl:text>
		        </xsl:when>
		        <xsl:when test="@strength='x-weak'">
          			<xsl:text>small</xsl:text>
		        </xsl:when>
		        <xsl:when test="@strength='strong'">
          			<xsl:text>large</xsl:text>
		        </xsl:when>
		        <xsl:when test="@strength='x-strong'">
          			<xsl:text>large</xsl:text>
		        </xsl:when>
		        <xsl:otherwise>
		          	<xsl:text>medium</xsl:text>
		        </xsl:otherwise>
		    </xsl:choose>
		</xsl:attribute>
	</xsl:if>
	<xsl:apply-templates />
</break>
</xsl:template>

</xsl:stylesheet>
