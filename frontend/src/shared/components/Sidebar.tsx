import { Link } from "react-router-dom"

const Sidebar = () => {
    return (
        <aside className="p-6 bg-secondary border-r-2 w-60">
            <Link to={"/"}>
            <div className="hover:bg-bg p-4 rounded-xl shadow-sm font-semibold border">
                CONTACTS
            </div>
            </Link>
        </aside>
    )
}

export default Sidebar