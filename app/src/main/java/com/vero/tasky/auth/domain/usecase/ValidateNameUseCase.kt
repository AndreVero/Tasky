package com.vero.tasky.auth.domain.usecase

class ValidateNameUseCase {
    operator fun invoke(name: String) : Boolean {
        return name.length in 4..50
    }

}