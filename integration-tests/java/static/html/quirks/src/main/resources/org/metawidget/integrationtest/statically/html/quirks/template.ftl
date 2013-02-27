<form>
	<table>
		<tbody>
			<#list widgets as widget><tr>
				<th>${widget.label}:</th>
				<td>${widget.xml}</td>
				<#if (widget.attributes.required!"false") == "true">
				<td>*</td>
				<#else>
				<td></td>
				</#if>
			</tr></#list>
		</tbody>
	</table>
</form>