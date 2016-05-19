package com.adaptc.gradle.moabsdk.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.pegdown.Extensions
import org.pegdown.PegDownProcessor

public class ProcessMarkdownTask extends DefaultTask {
	@InputFiles
	FileCollection getMarkdownFiles() {
		project.fileTree(dir: '.').matching {
			include 'README*.md'
		}
	}

	@OutputFiles
	FileCollection getHtmlFiles() {
		FileCollection result = project.files()
		result.from = markdownFiles.collect { File markdownFile ->
			markdownFile.name.replaceAll(/\.md$/, ".html")
		}
		result
	}

	@TaskAction
	void processMarkdown() {
		PegDownProcessor processor = new PegDownProcessor(Extensions.TABLES |
				Extensions.ABBREVIATIONS |
				Extensions.FENCED_CODE_BLOCKS |
				Extensions.SMARTYPANTS |
				Extensions.DEFINITIONS |
				Extensions.QUOTES
		)
		markdownFiles.each { File markdownFile ->
			File htmlFile = new File(markdownFile.parent, markdownFile.name.replaceAll(/\.md$/, ".html"))
			htmlFile.text = processor.markdownToHtml(markdownFile.text)
		}
	}
}
