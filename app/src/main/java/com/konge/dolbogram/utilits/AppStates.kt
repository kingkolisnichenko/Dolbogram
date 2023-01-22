package com.konge.dolbogram.utilits

enum class AppStates(val state: String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    companion object {
        fun updadeState(appStates: AppStates) {
            if (AUTH.currentUser != null) {
                REF_DATABASE_ROOT.child(NODE_USERS).child(UUID).child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener {
                        USER.state = appStates.state
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }
        }
    }
}