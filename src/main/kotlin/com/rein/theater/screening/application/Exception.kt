package com.rein.theater.screening.application

import com.rein.theater.screening.view.RegistScreeningRequest

class NotExistMovieException(id: Long) : RuntimeException("Does not exist movie. id=$id")

class FailedToRegistScreeningException(request: RegistScreeningRequest) : RuntimeException("Can't regist screening. screening=$request")
