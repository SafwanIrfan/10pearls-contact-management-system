import { Download, Upload, UserPlus } from "lucide-react";
import Button from "../../../shared/components/Button";
import { useNavigate } from "react-router-dom";

const ContactPageHeader = () => {
  const navigate = useNavigate();

  return (
    <section className="flex flex-col sm:flex-row sm:items-center gap-3 sm:gap-0 justify-between border-b border-gray-100">
      <h1 className="font-poppins text-xl sm:text-3xl font-semibold text-heading">
        Contact List
      </h1>

      {/* Actions */}
      <div className="flex items-center gap-2 flex-wrap">
        <Button icon={Upload} label="Import" />
        <Button icon={Download} label="Export" />
        <Button
          onClick={() => navigate("/contact/add")}
          icon={UserPlus}
          label="Add Contact"
          variant="primary"
        />
      </div>
    </section>
  );
};

export default ContactPageHeader;
