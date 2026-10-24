<%@ page pageEncoding="UTF-8" %>
<%-- Aviso LGPD + aceite dos termos. Antes do include: c:set textoLgpd --%>
<div class="aviso-lgpd" role="note">
    <img src="${ctx}/assets/icone-lock.svg" width="18" height="20" alt="">
    <p><strong>Seus dados estão protegidos.</strong> <c:out value="${textoLgpd}" /></p>
</div>

<div class="aceite-termos">
    <input type="checkbox" id="cad-termos" name="termos" value="aceito" class="checkbox-termos" required ${cad.termos == 'aceito' ? 'checked' : ''}>
    <label for="cad-termos">Li e aceito os <a href="${ctx}/documentos/termos-de-uso.pdf" target="_blank" rel="noopener">Termos de uso</a> e a <a href="${ctx}/documentos/politica-de-privacidade.pdf" target="_blank" rel="noopener">Política de privacidade</a>.</label>
</div>
