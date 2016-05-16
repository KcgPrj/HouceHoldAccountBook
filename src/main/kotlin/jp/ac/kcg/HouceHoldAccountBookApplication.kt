package jp.ac.kcg

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class HouceHoldAccountBookApplication

fun main(args: Array<String>) {
    SpringApplication.run(HouceHoldAccountBookApplication::class.java, *args)
}
