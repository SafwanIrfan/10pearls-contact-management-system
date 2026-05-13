import { Download, Upload, UserPlus } from "lucide-react";

const ContactPageHeader = () => {
  return (
    <section className="flex flex-col sm:flex-row sm:items-center gap-3 sm:gap-0 justify-between border-b border-gray-100">
      {/* Title */}
      <h1 className="font-poppins text-xl sm:text-3xl font-semibold text-heading">
        Contact List
      </h1>

      {/* Actions */}
      <div className="flex items-center gap-2 flex-wrap">
        <button className="flex items-center gap-2 px-3 sm:px-4 py-2 text-sm font-medium text-text bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
          <Upload size={14} strokeWidth={1.8} />
          <span className="hidden sm:inline">Import</span>
        </button>
        <button className="flex items-center gap-2 px-3 sm:px-4 py-2 text-sm font-medium text-text bg-white border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
          <Download size={14} strokeWidth={1.8} />
          <span className="hidden sm:inline">Export</span>
        </button>
        <button className="flex items-center gap-2 px-3 sm:px-4 py-2 text-sm font-medium text-white bg-button rounded-lg hover:bg-button/90 transition-colors">
          <UserPlus size={14} strokeWidth={1.8} />
          <span className="hidden sm:inline">Add Employee</span>
        </button>
      </div>
    </section>
  );
};

export default ContactPageHeader;
