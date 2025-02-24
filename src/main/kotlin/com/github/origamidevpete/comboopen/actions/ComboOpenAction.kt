package com.github.origamidevpete.comboopen.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.vfs.VirtualFileManager

class ComboOpenAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val fileEditorManager = FileEditorManagerEx.getInstanceEx(project)

        val filePathWithoutExtension = file.path.substringBeforeLast('.')

        val html = VirtualFileManager.getInstance().findFileByUrl("file://${filePathWithoutExtension}.html") ?: return
        val ts = VirtualFileManager.getInstance().findFileByUrl("file://${filePathWithoutExtension}.ts") ?: return
        val scss = VirtualFileManager.getInstance().findFileByUrl("file://${filePathWithoutExtension}.scss") ?: return

        val window = fileEditorManager.activeWindow.join() ?: return
        if (fileEditorManager.windows.size != 3) {
            fileEditorManager.unsplitAllWindow()
            fileEditorManager.createSplitter(1, window)
            fileEditorManager.createSplitter(0, fileEditorManager.getNextWindow(window))
        }

        fileEditorManager.currentWindow = window
        fileEditorManager.openFile(html, fileEditorManager.currentWindow)
        fileEditorManager.currentWindow = fileEditorManager.getNextWindow(fileEditorManager.currentWindow!!)
        fileEditorManager.openFile(ts, fileEditorManager.currentWindow)
        fileEditorManager.currentWindow = fileEditorManager.getNextWindow(fileEditorManager.currentWindow!!)
        fileEditorManager.openFile(scss, fileEditorManager.currentWindow)

//        Messages.showMessageDialog(e.project, file.path, "Welcome", null)
    }
}