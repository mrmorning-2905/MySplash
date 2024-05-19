package com.psd.learn.mysplash.utils.log

import java.util.Locale

class Logger private constructor(logWriter: LogWriter) {
    private val _logWriter: LogWriter

    init {
        _logWriter = logWriter
    }

    companion object {
        private const val VERBOSE = 2

        private const val DEBUG = 3

        private const val INFO = 4

        private const val WARN = 5

        private const val ERROR = 6

        private const val MAX_INDEX = 9999

        private var instance: Logger? = null

        private var _logIndex = 0

        private val logIndex: Int
            get() {
                _logIndex++
                if (_logIndex > MAX_INDEX) {
                    _logIndex = 0
                }
                return _logIndex
            }

        fun init(logWriter: LogWriter) {
            instance = Logger(logWriter)
        }

        private fun log(logLevel: Int, msg: String) {
            checkNotNull(instance!!._logWriter) {"Log class is not initialized!"}
            when (logLevel) {
                VERBOSE -> instance!!._logWriter.v(msg)
                DEBUG -> instance!!._logWriter.d(msg)
                INFO -> instance!!._logWriter.i(msg)
                WARN -> instance!!._logWriter.w(msg)
                ERROR -> instance!!._logWriter.e(msg)
            }
        }

        private fun getMsg(tag: String, msg: String): String {
            return "${String.format(Locale.ENGLISH, "[%04d/%-20s]", logIndex, tag)} $msg"
        }

        fun v(tag: String, msg: String) {
            log(VERBOSE, getMsg(tag, msg))
        }

        fun d(tag: String, msg: String) {
            log(DEBUG, getMsg(tag, msg))
        }

        fun w(tag: String, msg: String) {
            log(WARN, getMsg(tag, msg))
        }

        fun i(tag: String, msg: String) {
            log(INFO, getMsg(tag, msg))
        }

        fun e(tag: String, msg: String) {
            log(ERROR, getMsg(tag, msg))
        }
    }
}