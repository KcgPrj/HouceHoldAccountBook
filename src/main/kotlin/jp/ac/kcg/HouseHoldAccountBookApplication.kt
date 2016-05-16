package jp.ac.kcg

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class HouseHoldAccountBookApplication

fun main(args: Array<String>) {
    SpringApplication.run(HouseHoldAccountBookApplication::class.java, *args)
}
