package com.example.youtubeTrender.batch

import com.example.youtubeTrender.service.CsvService
import com.example.youtubeTrender.service.YoutubeService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class YoutubeBatch(
    private val youtubeService: YoutubeService,
    private val csvService: CsvService
) {
    private val log = LoggerFactory.getLogger(javaClass)

//    @Scheduled(fixedRate = 3600000) // 1시간마다 실행 (단위: 밀리초)
@Scheduled(fixedRate = 60000) // 1분마다 (단위: 밀리초)
fun fetchPopularCommentsBatch() {
        log.info("⏰ Youtube 댓글 수집 배치 시작: {}", LocalDateTime.now())

        val popularVideos = youtubeService.getPopularVideos(50)
        println("[popVidoes2]:$popularVideos")
        val allComments = popularVideos.flatMap { video ->
            try {
                youtubeService.getComments(video.videoId)
            } catch (e: Exception) {
                log.error("❌ 댓글 조회 실패 - videoId: ${video.videoId}, 에러: ${e.message}")
                emptyList()
            }
        }

        println("allComments")
        println(allComments)

        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))
        // 해당 위치에 저장.
        val fileName = "./comments_$timestamp.csv"
//        println("allComments")
//        println(allComments)

        csvService.writeCommentsToCsv(allComments, fileName)

        println("✅ 수집 완료. 총 ${allComments.size}개 댓글 저장 -> $fileName")
    }
}
