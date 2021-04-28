package com.darekbx.legopartscount.repository.rebrickable

import com.darekbx.legopartscount.repository.HeaderInterceptor

class AuthorizationInterceptor(authenticationKey: String) :
    HeaderInterceptor("Authorization", { "key $authenticationKey" })
