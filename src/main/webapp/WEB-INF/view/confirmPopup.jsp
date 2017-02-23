<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="confirmPopup" class="popupBack hidden">
    <div class="popup">
        <div class="jlab-row">
            <div class="jlab-cell-12 align-right">
                <a class="text-large" href="javascript:;" onclick="main.closeConfirmDialog()">&times;</a>
            </div>
        </div>
        <div class="jlab-row margin">
            <div class="jlab-cell-12 align-center text-medium">
                <span id="confirmPopupText"></span>
            </div>
        </div>
        <br>

        <div class="jlab-row margin">
            <div class="jlab-cell-6">

            </div>
            <div class="jlab-cell-3">
                <button class="jlab-cell-12" type="button" id="confirmPopup_ok">Ок</button>
            </div>
            <div class="jlab-cell-3">
                <button class="jlab-cell-12" type="button" id="confirmPopup_cancel" onclick="main.closeConfirmDialog()">Отмена</button>
            </div>
        </div>
    </div>
</div><%--div#confirmPopup end--%>