import LeadlyLogo from "./../../assets/LeadLy.svg";

const Navbar = () => {
  return (
    <nav className="p-6 bg-secondary border-b-2">
      <img src={LeadlyLogo} />
      {/* <text className="text-[#FFCE00] text-2xl font-poppins font-bold">
        Leadly
      </text> */}
    </nav>
  );
};

export default Navbar;
