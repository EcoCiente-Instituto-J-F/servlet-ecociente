<%@ page pageEncoding="UTF-8" %>
<%-- Canal dos avisos de coleta (chips) --%>
<c:set var="canal" value="${empty cad.avisos ? 'whatsapp' : cad.avisos}" />
<fieldset class="chips">
    <legend>Como prefere receber os avisos de coleta?</legend>
    <div class="chips-lista">
        <label class="chip"><input type="radio" name="avisos" value="whatsapp" ${canal == 'whatsapp' ? 'checked' : ''}>WhatsApp</label>
        <label class="chip"><input type="radio" name="avisos" value="email" ${canal == 'email' ? 'checked' : ''}>E-mail</label>
        <label class="chip"><input type="radio" name="avisos" value="sms" ${canal == 'sms' ? 'checked' : ''}>SMS</label>
    </div>
</fieldset>
