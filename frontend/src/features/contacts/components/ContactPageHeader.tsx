import { Download, Upload, UserPlus } from "lucide-react";
import Button from "../../../shared/components/Button";
import { useNavigate } from "react-router-dom";

interface ContactPageHeaderProps {
  onImport: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onExport: () => void;
  openFilePicker: () => void;
  fileInputRef: React.RefObject<HTMLInputElement>;
  importing: boolean;
  exporting: boolean;
}

const ContactPageHeader = ({
  onImport,
  onExport,
  openFilePicker,
  fileInputRef,
  importing,
  exporting,
}: ContactPageHeaderProps) => {
  const navigate = useNavigate();

  return (
    <section className="flex flex-col sm:flex-row sm:items-center gap-3 sm:gap-0 justify-between border-b border-gray-100">
      <h1 className="font-poppins text-xl sm:text-3xl font-semibold text-heading">
        Contact List
      </h1>

      <div className="flex items-center gap-2 flex-wrap">
        {/* Hidden file input */}
        <input
          ref={fileInputRef}
          type="file"
          accept=".csv"
          onChange={onImport}
          className="hidden"
        />

        <Button
          icon={Upload}
          label={importing ? "Importing..." : "Import"}
          onClick={openFilePicker}
          disabled={importing}
          hideLabel={true}
        />
        <Button
          icon={Download}
          label={exporting ? "Exporting..." : "Export"}
          onClick={onExport}
          disabled={exporting}
          hideLabel={true}
        />
        <Button
          icon={UserPlus}
          label="Add Contact"
          variant="primary"
          onClick={() => navigate("/contact/add")}
          hideLabel={true}
        />
      </div>
    </section>
  );
};

export default ContactPageHeader;
