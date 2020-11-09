package dev.olaore.unittestingonandroid.test_driven_development.solution_exercise_8

import dev.olaore.unittestingonandroid.test_driven_development.exercise8.contacts.Contact
import dev.olaore.unittestingonandroid.test_driven_development.exercise8.networking.ContactSchema
import dev.olaore.unittestingonandroid.test_driven_development.exercise8.networking.GetContactsHttpEndpoint
import dev.olaore.unittestingonandroid.test_driven_development.exercise8.networking.GetContactsHttpEndpoint.Callback
import dev.olaore.unittestingonandroid.test_driven_development.exercise8.networking.GetContactsHttpEndpoint.FailReason

class GetContactsUseCaseSync(
    private var httpEndpoint: GetContactsHttpEndpoint
) {

    private var listeners = mutableListOf<Listener>()

    companion object {

        enum class UseCaseResult { FAILURE, NETWORK_ERROR }

        interface Listener {
            fun onContactsProvided(contacts: List<Contact>)
            fun onErrorOccurred(result: UseCaseResult)
        }

    }

    fun getContactsSync(filterTerm: String) {
        httpEndpoint.getContacts(filterTerm, object : Callback {
            override fun onGetContactsSucceeded(cartItems: MutableList<ContactSchema>?) {
                listeners.forEach {
                    it.onContactsProvided(
                        cartItems!!.map { schema ->
                            Contact(schema.id, schema.fullName, schema.imageUrl)
                        }
                    )
                }
            }

            override fun onGetContactsFailed(failReason: GetContactsHttpEndpoint.FailReason?) {
                listeners.forEach {
                    if (failReason == FailReason.GENERAL_ERROR) {
                        it.onErrorOccurred(UseCaseResult.FAILURE)
                    } else if (failReason == FailReason.NETWORK_ERROR) {
                        it.onErrorOccurred(UseCaseResult.NETWORK_ERROR)
                    }
                }
            }
        })
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

}

