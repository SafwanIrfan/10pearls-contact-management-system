import LeadlyLogo from "./../../assets/LeadLy.svg";

const Navbar = () => {
  return (
    <nav className="p-6 bg-secondary border-b-2">
      <img alt="Leadly-logo" src={LeadlyLogo} />
    </nav>
  );
};

export default Navbar;
