import { Link } from "react-router-dom";

const Sidebar = () => {
  return (
    <aside className="p-6  border-r-2 w-auto">
      <Link className="" to={"/"}>
        <div className="border border-secondaryButton p-2 rounded shadow-sm transition-all hover-bg/80">
          <p className="font-poppins text-text">CONTACTS</p>
        </div>
      </Link>
    </aside>
  );
};

export default Sidebar;
