import {
  ChevronLeft,
  ChevronRight,
  Mail,
  Phone,
  RefreshCw,
  UserCircle2,
  MoreHorizontal,
} from "lucide-react";
import type {
  ContactResponseDTO,
  GetAllContactsResponse,
} from "../types/contact.types";
import { useContacts } from "../hooks/useContact";
import { Spinner } from "../../../shared/components/Spinner";
import Avatar from "./Avatar";
import { useNavigate } from "react-router-dom";

const EmptyState = () => (
  <tr>
    <td colSpan={4} className="py-16 text-center">
      <UserCircle2 size={40} className="mx-auto mb-3 text-gray-400" />
      <p className="text-sm font-medium text-gray-400">No contacts found</p>
      <p className="text-xs text-gray-400 mt-1">
        Try adjusting your search or filters
      </p>
    </td>
  </tr>
);

const ErrorState = ({ onRetry }: { onRetry: () => void }) => (
  <tr>
    <td colSpan={4} className="py-16 text-center">
      <p className="text-sm font-medium text-red-400 mb-3">
        Failed to load contacts
      </p>
      <button
        onClick={onRetry}
        className="inline-flex items-center cursor-pointer gap-2 px-4 py-2 text-sm font-medium text-text bg-white border border-gray-200 rounded-xl hover:bg-gray-50 transition-all"
      >
        <RefreshCw size={13} />
        Try again
      </button>
    </td>
  </tr>
);

const PAGE_SIZE_OPTIONS = [10, 20, 50];

const getPageNumbers = (current: number, total: number): (number | "...")[] =>
  Array.from({ length: total }, (_, i) => i + 1)
    .filter((p) => p === 1 || p === total || Math.abs(p - current) <= 1)
    .reduce<(number | "...")[]>((acc, p, i, arr) => {
      const prev = arr[i - 1];
      if (i > 0 && typeof prev === "number" && p - prev > 1) acc.push("...");
      acc.push(p);
      return acc;
    }, []);

interface PaginationProps {
  page: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (size: number) => void;
}

const Pagination = ({
  page,
  pageSize,
  totalElements,
  totalPages,
  onPageChange,
  onPageSizeChange,
}: PaginationProps) => (
  <div className="flex flex-col sm:flex-row items-center justify-between gap-3 px-4 py-3 border-t border-gray-100 bg-gray-50/40">
    <div className="flex items-center gap-3 text-xs text-gray-400">
      <span>
        Showing{" "}
        <span className="font-medium text-text">
          {(page - 1) * pageSize + 1}–{Math.min(page * pageSize, totalElements)}
        </span>{" "}
        of <span className="font-medium text-text">{totalElements}</span>
      </span>
      <span className="text-gray-200">|</span>
      <div className="flex items-center gap-1.5">
        <span>Rows</span>
        <select
          value={pageSize}
          onChange={(e) => onPageSizeChange(Number(e.target.value))}
          className="bg-white border border-gray-200 rounded-lg px-2 py-1 text-xs text-text outline-none focus:border-button cursor-pointer"
        >
          {PAGE_SIZE_OPTIONS.map((size) => (
            <option key={size} value={size}>
              {size}
            </option>
          ))}
        </select>
      </div>
    </div>

    <div className="flex items-center gap-1">
      <button
        onClick={() => onPageChange(1)}
        disabled={page === 1}
        className="hidden sm:block px-2 py-1.5 cursor-pointer text-xs text-text rounded-lg hover:bg-gray-100 disabled:opacity-80 disabled:pointer-events-none transition-all"
      >
        First
      </button>
      <button
        onClick={() => onPageChange(page - 1)}
        disabled={page === 1}
        className="p-1.5 rounded-lg cursor-pointer text-gray-400 hover:bg-gray-100 disabled:opacity-30 disabled:pointer-events-none transition-all"
      >
        <ChevronLeft size={14} />
      </button>

      {getPageNumbers(page, totalPages).map((p) =>
        p === "..." ? (
          <span key={`ellipsis-before-${p}`} className="px-2 text-gray-500">
            …
          </span>
        ) : (
          <button
            key={`page-${p}`}
            onClick={() => onPageChange(p)}
            className={`w-7 h-7 text-xs cursor-pointer rounded-lg transition-all font-medium ${page === p ? "bg-button text-white shadow-sm" : "text-gray-400 hover:bg-gray-100"}`}
          >
            {p}
          </button>
        ),
      )}

      <button
        onClick={() => onPageChange(page + 1)}
        disabled={page === totalPages}
        className="p-1.5 rounded-lg cursor-pointer text-gray-400 hover:bg-gray-100 disabled:opacity-30 disabled:pointer-events-none transition-all"
      >
        <ChevronRight size={14} />
      </button>
      <button
        onClick={() => onPageChange(totalPages)}
        disabled={page === totalPages}
        className="hidden sm:block px-2 py-1.5 b text-xs cursor-pointer text-text rounded-lg hover:bg-gray-100 disabled:opacity-80 disabled:pointer-events-none transition-all"
      >
        Last
      </button>
    </div>
    <div className="flex sm:hidden gap-2">
      <button
        onClick={() => onPageChange(1)}
        disabled={page === 1}
        className="block sm:hidden px-2 py-1.5 cursor-pointer text-xs text-text rounded-lg hover:bg-gray-100 disabled:opacity-80 disabled:pointer-events-none transition-all"
      >
        First
      </button>
      <button
        onClick={() => onPageChange(totalPages)}
        disabled={page === totalPages}
        className="block sm:hidden px-2 py-1.5 b text-xs cursor-pointer text-text rounded-lg hover:bg-gray-100 disabled:opacity-80 disabled:pointer-events-none transition-all"
      >
        Last
      </button>
    </div>
  </div>
);

interface ContactsTableProps {
  readonly contacts: GetAllContactsResponse;
  readonly loading: boolean;
  readonly page: number;
  readonly setPage: (page: number) => void;
  readonly pageSize: number;
  readonly onPageSizeChange: (size: number) => void;
}

// Main

const COLUMNS = ["Name", "Title", "Emails", "Phones"];

export default function ContactsTable({
  contacts,
  loading,
  page,
  setPage,
  pageSize,
  onPageSizeChange,
}: ContactsTableProps) {
  const { error, refetch } = useContacts();
  const navigate = useNavigate();

  const data = contacts?.data ?? [];
  const totalElements = contacts?.totalElements ?? data.length;
  const totalPages = Math.ceil(totalElements / pageSize);
  const paginated = contacts.data;

  const renderTableBody = () => {
    if (loading)
      return (
        <tr>
          <td colSpan={5} className="py-16 text-center">
            <div className="flex justify-center">
              <Spinner size={24} />
            </div>
          </td>
        </tr>
      );

    if (error) return <ErrorState onRetry={refetch} />;

    if (paginated.length === 0) return <EmptyState />;

    return paginated.map((contact: ContactResponseDTO) => {
      const fullName = `${contact.firstName} ${contact.lastName}`;
      return (
        <tr
          key={contact.id}
          onClick={() => navigate(`/contact/${contact.id}`)}
          className="border-b border-gray-50 hover:bg-gray-50/70 cursor-pointer transition-colors group"
        >
          <td className="px-4 py-3">
            <div className="flex items-center gap-3">
              <Avatar name={fullName} />
              <span className="font-medium text-heading truncate">
                {fullName}
              </span>
            </div>
          </td>

          <td className="px-4 py-3 text-gray-500">{contact.title || "—"}</td>

          <td className="px-4 py-3">
            <div className="flex flex-col gap-1">
              {contact.emails?.slice(0, 2).map((e) => (
                <span
                  key={e.email}
                  className="flex items-center gap-1.5 text-xs text-gray-500"
                >
                  <Mail size={11} className="shrink-0 text-gray-500" />
                  <span className="truncate max-w-[180px]">{e.email}</span>
                  <div className="p-1 rounded border border-secondaryButton">
                    <span className="text-[10px] text-gray-600 capitalize">
                      {e.label}
                    </span>
                  </div>
                </span>
              ))}
              {contact.emails?.length > 2 && (
                <span className="text-[10px] text-gray-500">
                  +{contact.emails.length - 2} more
                </span>
              )}
            </div>
          </td>

          <td className="px-4 py-3">
            <div className="flex flex-col gap-1">
              {contact.phones?.slice(0, 2).map((p) => (
                <span
                  key={p.phone}
                  className="flex items-center gap-1.5 text-xs text-gray-500"
                >
                  <Phone size={11} className="shrink-0 text-gray-500" />
                  <span>{p.phone}</span>
                  <div className="p-1 rounded border border-secondaryButton">
                    <span className="text-[10px] text-gray-600 capitalize">
                      {p.label}
                    </span>
                  </div>
                </span>
              ))}
              {contact.phones?.length > 2 && (
                <span className="text-[10px] text-gray-500">
                  +{contact.phones.length - 2} more
                </span>
              )}
            </div>
          </td>

          <td className="px-4 py-3">
            <button className="p-1.5 rounded-lg text-gray-300 hover:text-text hover:bg-gray-100 opacity-0 group-hover:opacity-100 transition-all">
              <MoreHorizontal size={15} />
            </button>
          </td>
        </tr>
      );
    });
  };

  return (
    <div className="w-full mt-8 bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden font-poppins">
      <div className="overflow-x-auto">
        <table className="w-full text-sm text-text">
          <thead>
            <tr className="border-b border-gray-100 bg-gray-50/60">
              {COLUMNS.map((col) => (
                <th
                  key={col}
                  className="px-4 py-3 text-left text-xs font-medium uppercase tracking-wide"
                >
                  {col}
                </th>
              ))}
              <th className="px-4 py-3 w-10" />
            </tr>
          </thead>

          <tbody>{renderTableBody()}</tbody>
        </table>
      </div>

      {!loading && !error && paginated.length > 0 && (
        <Pagination
          page={page}
          pageSize={pageSize}
          totalElements={totalElements}
          totalPages={totalPages}
          onPageChange={setPage}
          onPageSizeChange={onPageSizeChange}
        />
      )}
    </div>
  );
}
