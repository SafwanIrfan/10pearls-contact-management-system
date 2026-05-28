import LeadlyLogo from "./../../assets/LeadLy.svg";

const Navbar = () => {
  return (
    <nav className="p-6 bg-secondary border-b-2">
      <img src={LeadlyLogo} />
    </nav>
  );
};

export default Navbar;
