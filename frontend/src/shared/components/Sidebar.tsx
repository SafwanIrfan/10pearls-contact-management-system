import {
  PanelLeftClose,
  PanelLeftOpen,
  UserCircle2,
  Users,
} from "lucide-react";
import { Link, useLocation } from "react-router-dom";
import { useContacts } from "../../features/contacts/hooks/useContact";

const NAV_ITEMS = [
  { label: "Contacts", icon: Users, path: "/" },
  { label: "Profile", icon: UserCircle2, path: "/profile" },
];

const Sidebar = () => {
  const location = useLocation();
  const { collapsed, setCollapsed } = useContacts();

  return (
    <aside
      className={`
        h-full bg-white border-r border-gray-100 flex flex-col transition-all duration-300
        ${collapsed ? "w-16" : "w-52"}
      `}
    >
      {/* Collapse Toggle */}
      <div className="hidden sm:flex items-center justify-end p-3 border-b border-gray-100">
        <button
          onClick={() => setCollapsed((prev) => !prev)}
          className="p-1.5 rounded-lg text-gray-400 hover:text-text hover:bg-gray-100 transition-all"
        >
          {collapsed ? (
            <PanelLeftOpen size={16} />
          ) : (
            <PanelLeftClose size={16} />
          )}
        </button>
      </div>

      {/* Nav Items */}
      <nav className="flex flex-col gap-1 p-2 flex-1">
        {NAV_ITEMS.map(({ label, icon: Icon, path }) => {
          const active = location.pathname === path;
          return (
            <Link
              key={path}
              to={path}
              className={`
                flex mt-10 sm:mt-0 items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all
                ${
                  active
                    ? "bg-button/10 text-button"
                    : "text-gray-500 hover:bg-gray-100 hover:text-text"
                }
              `}
            >
              <Icon size={17} strokeWidth={1.8} className="shrink-0" />
              {!collapsed && <span className="font-poppins">{label}</span>}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
};

export default Sidebar;
