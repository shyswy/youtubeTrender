package com.example.youtubeTrender.service

import com.example.youtubeTrender.dto.CommentDto
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter

@Service
class CsvService {
    fun writeCommentsToCsv(comments: List<CommentDto>, filePath: String = "./comments.csv") {

        FileWriter(File(filePath)).use { writer ->
            writer.appendLine("video_id,author,text")
            comments.forEach { comment ->
                val cleanText = comment.text.replace("\n", " ").replace(",", " ")
                writer.appendLine("${comment.videoId},${comment.author},$cleanText")
            }
        }
    }
}
