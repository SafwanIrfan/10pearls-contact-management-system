import { useEffect, useState } from "react"
import { useContacts } from "../hooks/useContact"

const ContactsPage = () => {

    const { contacts, loading, error, refetch } = useContacts();

    return (

        <section className="flex-1 h-screen">
            <div className="flex w-min bg-button p-3 rounded-lg border shadow-lg ">
                <h1 className="text-heading text-2xl font-bold text-center">Contacts</h1>
            </div>

            <div className="flex w-full h-full mt-2">
                <div className="w-30 h-full p-3 rounded-lg border border-secondary-500">
                    <p>Contacts List</p>
                </div>

                <div className="w-full h-full p-3 rounded-lg border border-secondary-500">
                    <p>Contact Details</p>
                </div>

                <div className="w-full h-full p-3 rounded-lg border border-secondary-500">
                    <p>Contact Details</p>
                </div>
            </div>

            {/* {loading && <div>Loading...</div>}
            {error && <div>Error: {error}</div>}

            {
                contacts &&
                <div>
                    {contacts?.data?.map((contact) => (
                        <div key={contact.id}>
                            <div>{contact.firstName}</div>
                            <div>{contact.lastName}</div>
                            <div>{contact.title}</div>
                            <div>{contact?.emails?.map((email) => <span key={email.email}>{email.email}</span>)}</div>
                            <div>{contact?.phones?.map((phone) => <span key={phone.phone}>{phone.phone}</span>)}</div>
                        </div>
                    ))
                    }
                </div>
            } */}
        </section>
    )
}

export default ContactsPage
