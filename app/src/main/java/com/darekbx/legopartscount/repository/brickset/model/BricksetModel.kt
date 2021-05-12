package com.darekbx.legopartscount.repository.brickset.model

class Login(val apiKey: String, val username: String, val password: String)

class LoginResult(val status: String, val hash: String)

class GetSets(val apiKey: String, val userHash: String, val params: GetSetsParams)

class GetSetsParams(val pageSize: Int, val theme: String)

class GetSetsResult(val status: String, val matches: Int, val sets: List<Set>)

class Set(val number: String, val name: String, val year: Int, val pieces: Int, val image: Image)

class Image(val thumbnailURL: String)
