package io.asgardeo.android.core.util

import io.asgardeo.android.core.models.autheniticator.Authenticator

/**
 * Util class to handle the authenticator
 */
object AuthenticatorUtil {
    /**
     * Check whether there are duplicates authenticators of the given authenticator in the given step
     *
     * @param authenticators List of authenticators
     * @param authenticatorTypeString Authenticator type string
     *
     * @return Boolean value whether there are duplicates authenticators of the given authenticator in the given step
     */
    internal fun hasDuplicatesAuthenticatorsInGivenStep(
        authenticators: ArrayList<Authenticator>,
        authenticatorTypeString: String
    ): Boolean {
        return authenticators.count { it.authenticator == authenticatorTypeString } > 1
    }

    /**
     * Get the authenticator from the authenticator list
     *
     * @param authenticators List of authenticators
     * @param authenticatorTypeString Authenticator type string
     *
     * @return [Authenticator] object, `null` if the authenticator is not found
     */
    internal fun getAuthenticatorFromAuthenticatorsList(
        authenticators: ArrayList<Authenticator>,
        authenticatorTypeString: String
    ): Authenticator? {
        return authenticators.find { it.authenticator == authenticatorTypeString }
    }

    /**
     * Check whether there are duplicates authenticators of the given authenticator in the given step
     *
     * @param authenticators List of authenticators
     * @param authenticatorIdString Authenticator id string
     *
     * @return Boolean value whether there are duplicates authenticators of the given authenticator in the given step
     */
    private fun hasDuplicatesAuthenticatorsInGivenStepOnAuthenticatorId(
        authenticators: ArrayList<Authenticator>,
        authenticatorIdString: String
    ): Boolean {
        return authenticators.count { it.authenticatorId == authenticatorIdString } > 1
    }

    /**
     * Get the authenticator from the authenticators list
     *
     * @param authenticators List of authenticators
     * @param authenticatorIdString Authenticator id string
     *
     * @return [Authenticator] object, `null` if the authenticator is not found
     * or if there are duplicates authenticators of the given authenticator in the given step
     */
    private fun getAuthenticatorFromAuthenticatorsListOnAuthenticatorId(
        authenticators: ArrayList<Authenticator>,
        authenticatorIdString: String,
        authenticatorTypeString: String
    ): Authenticator? {
        val authenticator: Authenticator? =
            authenticators.find { it.authenticatorId == authenticatorIdString }

        val hasDuplicates: Boolean = hasDuplicatesAuthenticatorsInGivenStepOnAuthenticatorId(
            authenticators,
            authenticatorIdString
        )

        return if (hasDuplicates) null else {
            if (authenticator?.authenticator == authenticatorTypeString) authenticator
            else null
        }
    }

    /**
     * Get the authenticator  from the authenticator  list.
     * Done by checking the authenticator id or authenticator .
     *
     * Precedence: authenticatorId > authenticatorType
     *
     * @param authenticators List of authenticators
     * @param authenticatorIdString The authenticator id string
     * @param authenticatorTypeString The authenticator type string
     */
    internal fun getAuthenticatorFromAuthenticatorsList(
        authenticators: ArrayList<Authenticator>,
        authenticatorIdString: String,
        authenticatorTypeString: String
    ): Authenticator? = getAuthenticatorFromAuthenticatorsListOnAuthenticatorId(
        authenticators,
        authenticatorIdString,
        authenticatorTypeString
    )
}
